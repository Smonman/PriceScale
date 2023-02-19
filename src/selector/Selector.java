package selector;

import java.util.List;

/**
 * This represents a selector.
 *
 * <p>A selector selects a specific item from a given list.</p>
 */
public interface Selector<T> {

    /**
     * Sets the list for this selector.
     *
     * @param items the list
     */
    void set(List<T> items);

    /**
     * Selects the given item.
     *
     * @return the selected item
     */
    T select();
}
