package retailer.impl;

import article.Article;
import retailer.Retailer;
import shopping.cart.ShoppingCart;
import shopping.cart.impl.SimpleShoppingCart;
import shopping.list.ShoppingList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class Retailers {
    private static final List<Retailer> retailers = new LinkedList<>();

    private Retailers() {
    }

    public static void add(final Retailer retailer) {
        retailers.add(retailer);
    }

    public static List<ShoppingCart> compileCarts(final ShoppingList shoppingList) {
        for (int i = 0; i < retailers.size(); i++) {
            retailers.get(i).setId(i);
        }
        final List<ShoppingCart> carts = new LinkedList<>();
        for (final Retailer r : retailers) {
            carts.add(new SimpleShoppingCart(r));
        }
        List<Article> articles = new LinkedList<>();
        for (final String s : shoppingList) {
            setQuery(s);
            articles = lookup();
            for (int i = 0; i < carts.size(); i++) {
                carts.get(i).add(articles.get(i));
            }
        }
        return carts;
    }

    private static void setQuery(final String query) {
        for (final Retailer r : retailers) {
            r.setQuery(query);
        }
    }

    private static List<Article> lookup() {
        final Article[] articles = new Article[retailers.size()];
        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final CompletionService<Article> completionService =
            new ExecutorCompletionService<>(executorService);
        for (final Retailer r : retailers) {
            completionService.submit(r);
        }
        for (int i = 0; i < retailers.size(); i++) {
            try {
                final Future<Article> result = completionService.take();
                final Article resultArticle = result.get();
                articles[resultArticle.retailerId()] = resultArticle;
            } catch (Exception ignore) {
            }
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
        }
        return Arrays.stream(articles).toList();
    }
}
