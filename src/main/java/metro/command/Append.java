package metro.command;

import metro.service.MetroService;

import java.util.List;

/**
 * Adds a new station at the end of the line
 */
public class Append extends HyperMetroCommand {

    public Append(final MetroService metroService) {
        super(metroService);
    }

    @Override
    public String apply(final List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_TWO);
        metroService.append(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME));
        return "Metro station successfully added";
    }
}
