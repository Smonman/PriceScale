package shopping.list;

/**
 * This represents a shopping list.
 *
 * <p>A shopping list is a collection of wanted articles.</p>
 */
public interface ShoppingList extends Iterable<String> {

    /**
     * Adds the given item to the shopping list.
     *
     * @param article the given article
     */
    void addArticle(String article);
}
