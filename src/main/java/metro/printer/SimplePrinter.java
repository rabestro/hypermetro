package metro.printer;

import metro.model.MetroLine;
import metro.model.MetroStation;
import metro.model.StationId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

@Service("printer")
public class SimplePrinter implements PrinterService {
    private static final System.Logger LOGGER = System.getLogger(SimplePrinter.class.getName());

    private static final String PREFIX_PREV = "<---| ";
    private static final String PREFIX_NEXT = "--->| ";
    private static final String PREFIX_TRAN = "<---> ";

    @Override
    public String printLine(MetroLine metroLine) {
        return metroLine.stations().stream()
                .map(this::printStation)
                .collect(joining(lineSeparator()));
    }

    @Override
    public String printStation(MetroStation metroStation) {
        LOGGER.log(TRACE, "prints metro station: {0}", metroStation);
        final var name = metroStation.id().name();
        return name + lineSeparator()
                + printNeighbors(PREFIX_PREV, metroStation.prev())
                + printNeighbors(PREFIX_NEXT, metroStation.next())
                + printNeighbors(PREFIX_TRAN, metroStation.transfer());
    }

    @Override
    public String printRoute(List<StationId> route) {
        LOGGER.log(TRACE, "prints route: {0}", route);
        final var stringJoiner = new StringJoiner(System.lineSeparator());
        var line = route.get(0).line();

        for (final var node : route) {
            if (!node.line().equals(line)) {
                line = node.line();
                stringJoiner.add("Transition to line " + line);
            }
            stringJoiner.add(node.name());
            LOGGER.log(TRACE, "route metro station: {0}", node.name());
        }
        return stringJoiner.toString();
    }

    private String printNeighbors(final String prefix, final Set<StationId> stations) {
        final Function<StationId, String> name = prefix.equals(PREFIX_TRAN) ? StationId::line : StationId::name;
        return stations.isEmpty() ? "" : stations.stream()
                .map(name.andThen(prefix::concat))
                .collect(joining(lineSeparator())) + lineSeparator();
    }

}
