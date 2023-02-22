package gui.util;

import article.Article;
import gui.table.ResultTable;
import shopping.cart.ShoppingCart;
import shopping.list.ShoppingList;

import java.util.LinkedList;
import java.util.List;

public final class ResultTableFactory {

    private static final String HEADER = "Articles";
    private static final String FOOTER = "Total";
    private static final String NO_ARTICLE = "-";
    private static final ResultTable RESULT_TABLE = new ResultTable();
    private static ShoppingList shoppingList;
    private static List<ShoppingCart> shoppingCarts = null;

    private ResultTableFactory() {
    }

    public static ResultTable get() {
        return RESULT_TABLE;
    }

    public static void setShoppingList(final ShoppingList shoppingList) {
        ResultTableFactory.shoppingList = shoppingList;
    }

    public static void addShoppingCart(final ShoppingCart shoppingCart) {
        if (ResultTableFactory.shoppingCarts == null) {
            ResultTableFactory.shoppingCarts = new LinkedList<>();
        }
        ResultTableFactory.shoppingCarts.add(shoppingCart);
    }

    public static void addShoppingCarts(final List<ShoppingCart> shoppingCarts) {
        if (ResultTableFactory.shoppingCarts == null) {
            ResultTableFactory.shoppingCarts = new LinkedList<>();
        }
        ResultTableFactory.shoppingCarts.addAll(shoppingCarts);
    }

    public static void compileTable() {
        RESULT_TABLE.addColumn(HEADER, compileFirstColumn());
        RESULT_TABLE.addColumns(compileHeaders(), compileColumns());
    }

    private static List<List<Object>> compileColumns() {
        final List<List<Object>> columns = new LinkedList<>();
        for (final ShoppingCart sc : shoppingCarts) {
            columns.add(compileColumn(sc));
        }
        return columns;
    }

    private static List<Object> compileColumn(final ShoppingCart shoppingCart) {
        final List<Object> column = new LinkedList<>();
        for (final Article a : shoppingCart) {
            if (a != null) {
                column.add(a.name());
            } else {
                column.add(NO_ARTICLE);
            }
        }
        column.add(shoppingCart.getTotal());
        return column;
    }

    private static List<String> compileHeaders() {
        final List<String> headers = new LinkedList<>();
        for (final ShoppingCart sc : shoppingCarts) {
            headers.add(compileHeader(sc));
        }
        return headers;
    }

    private static String compileHeader(final ShoppingCart shoppingCart) {
        return shoppingCart.getRetailer().toString();
    }

    private static List<Object> compileFirstColumn() {
        final List<Object> firstColum = new LinkedList<>();
        for (final String s : shoppingList) {
            firstColum.add(s);
        }
        firstColum.add(FOOTER);
        return firstColum;
    }
}
