package retailer.impl;

import article.Article;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selector.ArticleSelectors;
import util.exception.ParsingException;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a concrete implementation of base retailer.
 */
public class Billa extends BaseRetailer {

    private static final Logger
        LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * This is the endpoint pattern for the product search.
     *
     * <p>The pattern has to be called with the query string, and this endpoint
     * is configured to return 20 results</p>
     */
    private static final String SEARCH_ENDPOINT_PATTERN =
        "https://www.billa.at/api/products/search/%s?page=0&pageSize=20";

    /**
     * This is the endpoint for details for a specific product.
     *
     * <p>This pattern has to be called with the product id.</p>
     */
    private static final String DETAILS_ENDPOINT_PATTERN =
        "https://shop.billa.at/api/articles/%s";

    /**
     * This is the product URL pattern.
     *
     * <p>This is the URL to find a given article. This pattern has to be
     * called with the product id.</p>
     */
    private static final String ARTICLE_URL_PATTERN =
        "https://www.billa.at/produkte/%s";

    public Billa() {
        super("Billa");
    }

    private String getSearchEndpoint(final String encodedQuery) {
        return String.format(SEARCH_ENDPOINT_PATTERN, encodedQuery);
    }

    private String getDetailsEndpoint(final String id) {
        return String.format(DETAILS_ENDPOINT_PATTERN, id);
    }

    /**
     * Searches for the given query.
     *
     * @param query the search query
     * @return the search results as a string.
     */
    private String search(final String query) {
        LOGGER.trace("search(String)");

        final String encodedQuery =
            URLEncoder.encode(query, StandardCharsets.UTF_8);
        return getUrlGrabber().grab(getSearchEndpoint(encodedQuery));
    }

    /**
     * Parses the search results.
     *
     * <p>Search results has to be a JSON String. If, for a given article, the
     * URL cannot be extracted, the article is skipped.</p>
     *
     * @param searchResult the search result JSON string.
     * @return a list of parsed article resources
     * @throws ParsingException if an error occurs while trying to parse JSON
     */
    private List<String> parseSearchResult(final String searchResult)
        throws ParsingException {
        LOGGER.trace("parseSearchResult(String)");

        JSONObject searchResultObject;
        JSONArray searchResults;
        try {
            searchResultObject = new JSONObject(searchResult);
            searchResults = searchResultObject.getJSONArray("results");
        } catch (JSONException e) {
            LOGGER.error("cannot parse JSON", e);
            throw new ParsingException(e);
        }
        final List<String> results = new LinkedList<>();
        for (final Object o : searchResults) {
            try {
                final String urlString = ((JSONObject) o).getString("url");
                results.add(urlString);
            } catch (JSONException | ClassCastException e) {
                LOGGER.error("cannot produce URL string", e);
                LOGGER.info("skipping URL");
            }
        }
        return results;
    }

    /**
     * Gets the resources for each URL.
     *
     * <p>The article ID is extracted from each url to get specific details via
     * the details endpoint.</p>
     *
     * @param urls a list of URL strings
     * @return a list of resources
     */
    private List<String> getArticleResource(final List<String> urls) {
        LOGGER.trace("getArticleResults(List<String>)");

        final List<String> results = new LinkedList<>();
        for (final String url : urls) {
            final String[] parts = url.split("/");
            final String id = parts[parts.length - 1];
            final String result = getUrlGrabber().grab(getDetailsEndpoint(id));
            results.add(result);
        }
        return results;
    }

    /**
     * Parses all given article results.
     *
     * <p>If an error occurs while trying to parse a specific article, this
     * article will be skipped.</p>
     *
     * @param articleResources all article resource strings
     * @return a list of articles
     */
    private List<Article> parseArticleResources(final List<String> articleResources) {
        LOGGER.trace("parseArticleResources(List<String>)");

        final List<Article> articles = new LinkedList<>();
        for (final String r : articleResources) {
            try {
                articles.add(parseArticleResource(r));
            } catch (ParsingException e) {
                LOGGER.error("error while parsing article", e);
                LOGGER.info("skipping article");
            }
        }
        return articles;
    }

    /**
     * Parses the given article resource.
     *
     * @param articleResource the article resource to be parsed
     * @return the parsed article
     * @throws ParsingException if the article cannot be parsed
     */
    private Article parseArticleResource(final String articleResource)
        throws ParsingException {
        LOGGER.trace("parseArticleResource(String)");

        String name;
        double price;
        double kiloPrice;
        String url;
        try {
            final JSONObject articleObject = new JSONObject(articleResource);
            name = articleObject.getString("name");
            price = articleObject.getJSONObject("price").getDouble("normal");
            final JSONObject priceObject = articleObject.getJSONObject("price");
            final boolean hasKiloPrice =
                priceObject.has("unit") && !priceObject.isNull("unit");
            if (hasKiloPrice) {
                LOGGER.debug("has kilo price");

                final String kiloPriceString = priceObject.getString("unit");
                final Pattern pattern = Pattern.compile("\\d+[\\.|,]\\d+");
                final Matcher matcher = pattern.matcher(kiloPriceString);
                try {
                    if (matcher.find()) {
                        kiloPrice =
                            Double.parseDouble(matcher.group()
                                                      .replace(",", "."));
                    } else {
                        LOGGER.debug("cannot find kilo price, using price");

                        kiloPrice = price;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("cannot parse kilo price", e);
                    LOGGER.info("using price");
                    kiloPrice = price;
                }
            } else {
                LOGGER.debug("does not have kilo price, using price");

                kiloPrice = price;
            }
            final String id = articleObject.getString("articleId");
            url = String.format(ARTICLE_URL_PATTERN, id);
        } catch (JSONException e) {
            LOGGER.error("cannot parse article", e);
            throw new ParsingException(e);
        }
        return new Article(name, price, kiloPrice, url, getId());
    }

    @Override
    public Article lookup() {
        LOGGER.trace("lookup()");

        try {
            final String searchResult = search(getQuery());
            final List<String> articleUrls = parseSearchResult(searchResult);
            final List<String> articleResults = getArticleResource(articleUrls);
            final List<Article> articles =
                parseArticleResources(articleResults);
            return ArticleSelectors.select(articles,
                                           ArticleSelectors.ArticleSelectorTypes.CHEAPEST_KILO);
        } catch (ParsingException e) {
            LOGGER.error("error occurred while lookup", e);
            LOGGER.info("returning null");
            return null;
        }
    }
}
