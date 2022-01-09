package metro.command;

import metro.model.StationId;
import metro.service.MetroService;

import java.util.List;

/**
 * Connects two metro stations
 */
public class Connect extends HyperMetroCommand {
    public Connect(final MetroService metroService) {
        super(metroService);
    }

    @Override
    public String apply(final List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_FOUR);
        final var source = new StationId(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME));
        final var target = new StationId(parameters.get(TARGET_LINE), parameters.get(TARGET_NAME));

        metroService.connect(source, target);

        return "Metro stations successfully connected";
    }
}
