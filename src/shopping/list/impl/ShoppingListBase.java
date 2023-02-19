package shopping.list.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a base implementation of the shopping list.
 */
public abstract class ShoppingListBase implements shopping.list.ShoppingList {

    private final List<String> articleNames;

    public ShoppingListBase() {
        this.articleNames = new LinkedList<>();
    }

    @Override
    public void addArticle(final String article) {
        articleNames.add(article);
    }

    @Override
    public Iterator<String> iterator() {
        return articleNames.iterator();
    }
}
