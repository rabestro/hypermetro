package metro.command;

import metro.model.MetroStation;
import metro.model.StationId;
import metro.service.MetroService;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class Print extends HyperMetroCommand {
    private static final String PREFIX_PREV = "<---| ";
    private static final String PREFIX_NEXT = "--->| ";
    private static final String PREFIX_TRAN = "<---> ";

    public Print(final MetroService metroService) {
        super(metroService);
    }

    @Override
    public String apply(final List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_TWO);
        final var stationId = new StationId(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME));
        final var station = metroService.getMetroStation(stationId);
        return printStation(station);
    }

    private String printStation(final MetroStation metroStation) {
        final var name = metroStation.id().name();
        return name + lineSeparator()
                + printNeighbors(PREFIX_PREV, metroStation.prev())
                + printNeighbors(PREFIX_NEXT, metroStation.next())
                + printNeighbors(PREFIX_TRAN, metroStation.transfer())
                + "..... " + metroStation.time();
    }

    private String printNeighbors(final String prefix, final Set<StationId> stations) {
        final Function<StationId, String> name = prefix.equals(PREFIX_TRAN) ? StationId::line : StationId::name;
        return stations.isEmpty() ? "" : stations.stream()
                .map(name.andThen(prefix::concat))
                .collect(joining(lineSeparator())) + lineSeparator();
    }
}
