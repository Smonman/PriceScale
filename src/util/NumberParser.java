package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.exception.ParsingException;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberParser {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Pattern pattern = Pattern.compile("\\d*[\\.|,]\\d+");

    private NumberParser() {
    }

    public static double getDoubleFromString(final String string)
        throws ParsingException {
        LOGGER.trace("getDoubleFromString(String)");

        final Matcher matcher = pattern.matcher(string);
        try {
            if (matcher.find()) {
                return Double.parseDouble(matcher.group().replace(",", "."));
            } else {
                LOGGER.warn("cannot find double in string");
                throw new ParsingException("cannot find double in string");
            }
        } catch (NumberFormatException e) {
            LOGGER.error("cannot parse number", e);
            throw new ParsingException("cannot parse number");
        }
    }
}
