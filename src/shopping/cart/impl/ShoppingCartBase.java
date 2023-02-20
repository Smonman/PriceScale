package shopping.cart.impl;

import article.Article;
import retailer.Retailer;
import shopping.cart.ShoppingCart;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This is a base implementation of shopping cart.
 */
public abstract class ShoppingCartBase implements ShoppingCart {
    private final List<Article> articles;
    private final Retailer retailer;

    protected ShoppingCartBase(final Retailer retailer) {
        this.articles = new LinkedList<>();
        this.retailer = retailer;
    }

    @Override
    public void add(Article article) {
        articles.add(article);
    }

    @Override
    public double getTotal() {
        return articles.stream()
                       .filter(Objects::nonNull)
                       .map(Article::price)
                       .reduce(0.0, Double::sum);
    }

    @Override
    public Retailer getRetailer() {
        return retailer;
    }

    @Override
    public Iterator<Article> iterator() {
        return articles.iterator();
    }

    @Override
    public boolean isComplete() {
        return articles.stream()
                       .noneMatch(Objects::isNull);
    }

    @Override
    public int compareTo(ShoppingCart o) {
        return Double.compare(getTotal(), o.getTotal());
    }
}
