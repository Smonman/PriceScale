package retailer.impl;

import article.Article;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selector.ArticleSelectors;
import util.NumberParser;
import util.exception.ParsingException;

import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a concrete implementation of base retailer.
 */
public class Spar extends BaseRetailer {

    private static final Logger
        LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * This is the endpoint pattern for the product search.
     *
     * <p>The pattern has to be called with the query string, and this endpoint
     * is configured to return 20 results</p>
     */
    private static final String SEARCH_ENDPOINT_PATTERN =
        "https://search-spar.spar-ics.com/fact-finder/rest/v4/search/products_lmos_at?query=%s&page=1&hitsPerPage=20&useAsn=false&substringFilter=title%3A!product-number";

    /**
     * This is the product URL pattern.
     *
     * <p>This is the URL to find a given article. This pattern has to be
     * called with the product id.</p>
     */
    private static final String ARTICLE_URL_PATTERN =
        "https://www.interspar.at/shop/lebensmittel%s";

    public Spar() {
        super("Spar");
    }

    private String getSearchEndpoint(final String encodedQuery) {
        return SEARCH_ENDPOINT_PATTERN.replace("%s", encodedQuery);
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
     * <p>Search results has to be a JSON string.</p>
     *
     * @param searchResult the search result JSON string.
     * @return a json array of articles
     * @throws ParsingException if an error occurs while trying to parse JSON
     */
    private JSONArray parseSearchResult(final String searchResult)
        throws ParsingException {
        LOGGER.trace("parseSearchResult(String)");

        try {
            final JSONObject searchResultObject = new JSONObject(searchResult);
            return searchResultObject.getJSONArray("hits");
        } catch (JSONException e) {
            LOGGER.error("cannot parse JSON", e);
            throw new ParsingException(e);
        }
    }

    /**
     * Parses the given JSON array.
     *
     * @param articleResources a JSON array of articles
     * @return a list of parsed articles
     */
    private List<Article> parseArticleResources(final JSONArray articleResources) {
        LOGGER.trace("parseArticleResources(JSONArray)");

        final List<Article> articles = new LinkedList<>();
        for (final Object o : articleResources) {
            try {
                articles.add(parseArticleResource(((JSONObject) o).getJSONObject(
                    "masterValues")));
            } catch (ParsingException | JSONException | ClassCastException e) {
                LOGGER.error("cannot parse article", e);
                LOGGER.info("skipping URL");
            }
        }
        return articles;
    }

    /**
     * Parses a given article resource.
     *
     * @param articleObject the given article JSON object
     * @return the parsed article
     * @throws ParsingException if the article cannot be parsed
     */
    private Article parseArticleResource(final JSONObject articleObject)
        throws ParsingException {
        LOGGER.trace("parseArticleResource(JSONObject)");

        String name;
        double price;
        double kiloPrice;
        String url;
        try {
            name = articleObject.getString("name");
            price = articleObject.getDouble("price");
            final String kiloPriceString =
                articleObject.getString("price-per-unit");
            try {
                kiloPrice = NumberParser.getDoubleFromString(kiloPriceString);
            } catch (ParsingException | NullPointerException e) {
                LOGGER.error("cannot parse kilo price string", e);
                LOGGER.info("using price as kilo price");
                kiloPrice = price;
            }
            final String urlPart = articleObject.getString("url");
            url = String.format(ARTICLE_URL_PATTERN, urlPart);
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
            final JSONArray articleResults = parseSearchResult(searchResult);
            if (articleResults.length() <= 0) {
                LOGGER.warn("no results found");
                LOGGER.info("returning null");
                return null;
            }
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
