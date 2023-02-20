package shopping.cart;

import article.Article;
import retailer.Retailer;

/**
 * This represents a shopping cart.
 *
 * <p>A shopping cart is not the same as a shopping list. A shopping cart is a
 * collection of the actual available items, not the wanted items.</p>
 *
 * @see shopping.list.ShoppingList
 */
public interface ShoppingCart extends Iterable<Article> {

    /**
     * Add another article to this.
     *
     * @param article the article to be added.
     */
    void add(Article article);

    /**
     * Gets the total of this cart.
     *
     * <p>The price used for this calculation is the price per piece.</p>
     *
     * @return the total of this cart.
     */
    double getTotal();

    /**
     * Gets the retailer associated with this cart.
     *
     * @return the associated retailer
     */
    Retailer getRetailer();
}
