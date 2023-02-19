package selector.impl;

import article.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This is a concrete selector.
 *
 * <p>This selector selects the article with the cheapest kilo price.</p>
 */
public class CheapestKiloSelector extends BaseSelector {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CheapestKiloSelector(final List<Article> articles) {
        super(articles);
    }

    @Override
    public Article select() {
        LOGGER.trace("select()");

        Article record = getItems().get(0);
        for (final Article i : getItems()) {
            if (i.kiloPrice() < record.kiloPrice()) {
                record = i;
            }
        }
        return record;
    }
}
