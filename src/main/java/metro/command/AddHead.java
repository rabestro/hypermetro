package metro.command;

import metro.service.MetroService;

import java.util.List;

/**
 * Adds a new station at the beginning of the metro line
 */
public class AddHead extends HyperMetroCommand {
    public AddHead(final MetroService metroService) {
        super(metroService);
    }

    @Override
    public String apply(final List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_TWO);
        metroService.addHead(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME));
        return "Metro station successfully added";
    }
}
