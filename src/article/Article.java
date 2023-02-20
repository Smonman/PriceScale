package article;

/**
 * This represents an article.
 *
 * <p>The standardized unit is 1 kg.</p>
 *
 * @param name       the name of the article
 * @param price      the price of the article
 * @param kiloPrice  the standardized unit price of the article
 * @param url        the URL of the article
 * @param retailerId the internal retailer ID of the article
 * @see retailer.Retailer
 */
public record Article(String name,
                      double price,
                      double kiloPrice,
                      String url,
                      int retailerId
) {
}
