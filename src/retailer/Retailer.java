package retailer;

import article.Article;

import java.util.concurrent.Callable;

/**
 * This represents a retailer.
 */
public interface Retailer extends Callable<Article> {

    /**
     * Sets the query string for this retailer.
     *
     * <p>The string must not be empty.</p>
     *
     * @param query the query string
     */
    void setQuery(String query);

    /**
     * Looks up the previously given query string.
     *
     * @return the article based on a selector.
     * @see selector.Selector
     */
    Article lookup();

    /**
     * Gets the ID of this retailer.
     *
     * @return the ID of this
     */
    int getId();

    /**
     * Sets the ID of this retailer.
     *
     * <p>The ID must not be negative.</p>
     *
     * @param id the new ID.
     */
    void setId(int id);
}
