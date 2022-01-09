package metro.command;

import metro.model.StationId;
import metro.service.MetroService;

import java.util.List;

/**
 * Removes a metro station
 */
public class Remove extends HyperMetroCommand {
    public Remove(final MetroService metroService) {
        super(metroService);
    }

    @Override
    public String apply(final List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_TWO);
        metroService.remove(new StationId(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME)));
        return "Metro station successfully removed";
    }
}
