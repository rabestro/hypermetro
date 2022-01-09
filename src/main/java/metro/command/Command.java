package metro.command;

import java.util.List;
import java.util.function.Function;

/**
 * The command accepts a list of parameters as input.
 * The result of the command is a string that is displayed on the screen.
 */
@FunctionalInterface
public interface Command extends Function<List<String>, String> {
}
