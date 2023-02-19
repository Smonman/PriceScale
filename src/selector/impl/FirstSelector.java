package selector.impl;

import article.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This is a concrete selector.
 *
 * <p>This selector always selects the first article.</p>
 */
public class FirstSelector extends BaseSelector {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FirstSelector(final List<Article> articles) {
        super(articles);
    }

    /**
     * Selects the first item of the list.
     *
     * @return the selected item or null if the list is empty.
     */
    @Override
    public Article select() {
        LOGGER.trace("select()");

        if (getItems().size() >= 1) {
            return getItems().get(0);
        }
        return null;
    }
}
