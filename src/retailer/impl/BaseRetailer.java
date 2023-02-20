package retailer.impl;

import article.Article;
import grabber.Grabber;
import grabber.impl.SimpleURLGrabber;
import retailer.Retailer;

/**
 * This is a base implementation of Retailer.
 */
public abstract class BaseRetailer implements Retailer {

    private final Grabber urlGrabber;
    private final String name;
    private int id;
    private String query;

    public BaseRetailer(final String name) {
        this.urlGrabber = new SimpleURLGrabber();
        this.name = name;
    }

    protected Grabber getUrlGrabber() {
        return urlGrabber;
    }

    protected final String getQuery() {
        return query;
    }

    @Override
    public final void setQuery(String query) {
        this.query = query;
    }

    @Override
    public final int getId() {
        return id;
    }

    @Override
    public final void setId(int id) {
        this.id = id;
    }

    @Override
    public Article call() throws Exception {
        return lookup();
    }

    @Override
    public String toString() {
        return name;
    }
}
