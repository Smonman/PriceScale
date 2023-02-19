package grabber.impl;

import grabber.Grabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;

/**
 * This is a concrete implementation of a grabber.
 *
 * <p>This grabber simply gets the resource at the given URL.</p>
 */
public class SimpleURLGrabber implements Grabber {
    private static final Logger
        LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String grab(final String urlString) {
        LOGGER.trace("grab(String)");

        HttpURLConnection connection = null;
        BufferedReader in = null;
        String inputLine;
        String content;
        try {
            final URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            final int status = connection.getResponseCode();
            in =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder contentBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                contentBuilder.append(inputLine);
            }
            content = contentBuilder.toString();
        } catch (UnknownServiceException e) {
            LOGGER.error("protocol does not support input", e);
            content = null;
        } catch (IOException e) {
            LOGGER.error("an IO exception occurred", e);
            content = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("error while closing buffered reader", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }
}
