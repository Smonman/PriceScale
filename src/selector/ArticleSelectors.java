package selector;

import article.Article;
import selector.impl.CheapestKiloSelector;
import selector.impl.CheapestSelector;
import selector.impl.FirstSelector;

import java.util.List;

/**
 * This is a utility class.
 *
 * <p>This class provides functionality to dynamically select a specific
 * article
 * from a given list based on the given selector.</p>
 */
public final class ArticleSelectors {
    private ArticleSelectors() {
    }

    public static Article select(final List<Article> articles,
                                 final ArticleSelectorTypes type
    ) {
        switch (type) {
            case CHEAPEST_KILO -> {
                return new CheapestKiloSelector(articles).select();
            }
            case CHEAPEST -> {
                return new CheapestSelector(articles).select();
            }
            case FIRST -> {
                return new FirstSelector(articles).select();
            }
            default -> {
                return new FirstSelector(articles).select();
            }
        }
    }

    public enum ArticleSelectorTypes {CHEAPEST, CHEAPEST_KILO, FIRST}
}
