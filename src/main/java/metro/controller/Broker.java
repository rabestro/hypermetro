package metro.controller;

import lombok.AllArgsConstructor;
import metro.command.Command;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

@AllArgsConstructor
public class Broker implements UnaryOperator<String> {
    private static final System.Logger LOGGER = System.getLogger("Request");
    private static final Pattern COMMAND_PATTERN =
            Pattern.compile("/?(?<command>[-\\w]+)(?:\\s+(?<parameters>.*))?");
    private static final Command INVALID_COMMAND = args -> "Invalid Command";

    private final ParameterParser parameterParser = new ParameterParser();
    private final Map<String, Command> actions;

    @Override
    public String apply(final String userInput) {
        final var matcher = COMMAND_PATTERN.matcher(userInput);
        final var command = getCommand(matcher);
        final var parameters = parameterParser.parse(matcher.group("parameters"));
        try {
            LOGGER.log(DEBUG, "Execute: {0}", userInput);
            return command.apply(parameters);
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException exception) {
            LOGGER.log(WARNING, exception::getMessage);
            return exception.getMessage();
        }
    }

    private Command getCommand(final Matcher matcher) {
        if (!matcher.matches()) {
            return INVALID_COMMAND;
        }
        final var commandName = matcher.group("command").toLowerCase();
        return actions.getOrDefault(commandName, INVALID_COMMAND);
    }

}
