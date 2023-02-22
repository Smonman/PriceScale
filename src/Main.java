import article.Article;
import gui.Window;
import retailer.impl.Billa;
import retailer.impl.Penny;
import retailer.impl.Retailers;
import retailer.impl.Spar;
import shopping.cart.ShoppingCart;
import shopping.list.ShoppingList;
import shopping.list.impl.SimpleShoppingList;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final Window window = new Window();

        final ShoppingList shoppingList = new SimpleShoppingList();
        shoppingList.addArticle("Milch");
        shoppingList.addArticle("Butter");
        shoppingList.addArticle("Sauerrahm");
        shoppingList.addArticle("Karotten");
        shoppingList.addArticle("Cola");
        Retailers.add(new Billa());
        Retailers.add(new Penny());
        Retailers.add(new Spar());
        final List<ShoppingCart> shoppingCarts =
            Retailers.compileCarts(shoppingList);
        Collections.sort(shoppingCarts);

        window.setTableData(shoppingList, shoppingCarts);

        for (final ShoppingCart c : shoppingCarts
        ) {
            System.out.println(c.getRetailer());
            for (final Article a : c) {
                System.out.println(a);
            }
            System.out.printf("%.2f € \n", c.getTotal());
        }
    }
}
