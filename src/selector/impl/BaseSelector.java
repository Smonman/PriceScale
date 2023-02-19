package selector.impl;

import article.Article;
import selector.Selector;

import java.util.List;

/**
 * This is a concrete base implementation of a selector.
 *
 * <p>This is an article selector</p>
 * @see Article
 */
public abstract class BaseSelector implements Selector<Article> {

    private List<Article> articles;

    public BaseSelector(final List<Article> articles) {
        this.articles = articles;
    }

    protected List<Article> getItems() {
        return articles;
    }

    @Override
    public void set(List<Article> articles) {
        this.articles = articles;
    }
}
