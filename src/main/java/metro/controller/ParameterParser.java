package metro.controller;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Parse and prepare a command parameters
 * <p>
 * Note that if the name of the line or station consists of several words,
 * the user should write it in quotation marks (after, they should be excluded from the name).
 * If the name of a station consists of one word it should be parseable with or without quotes.
 */
class ParameterParser {
    private static final String WITHOUT_QUOTES = "(\\w+)";
    private static final int WITHOUT_GROUP = 1;
    private static final String WITH_QUOTES = "\"([^\"]+)\"";
    private static final int WITH_GROUP = 2;

    private static final Pattern PARAMETERS_PATTERN =
            Pattern.compile(WITHOUT_QUOTES + "|" + WITH_QUOTES);

    private String extractParameter(final MatchResult result) {
        return Objects.requireNonNullElse(result.group(WITHOUT_GROUP), result.group(WITH_GROUP));
    }

    public List<String> parse(final String parameters) {
        if (parameters == null) {
            return List.of();
        }
        return PARAMETERS_PATTERN
                .matcher(parameters)
                .results()
                .map(this::extractParameter)
                .toList();
    }

}
