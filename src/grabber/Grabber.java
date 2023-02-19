package grabber;

/**
 * This represents a grabber.
 *
 * <p>A grabber can grab a specific resource from a given URL.</p>
 */
public interface Grabber {

    /**
     * Grabs the resource located at the given URL.
     *
     * <p>If the resource cannot be fetched, this will return {@code null}.</p>
     *
     * @param url the URL of the wanted resource
     * @return the resource at the given URL
     */
    String grab(String url);
}
