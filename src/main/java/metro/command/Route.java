package metro.command;

import metro.algorithm.SearchAlgorithm;
import metro.model.StationId;
import metro.service.MetroService;

import java.util.List;
import java.util.StringJoiner;

import static java.lang.System.Logger.Level.DEBUG;

/**
 * Finds and prints the route between two metro stations
 * by using one of the two search algorithms.
 * <p>
 * The travel time between station may or may not be taken into account.
 * Transferring from one line to another is configurable.
 * <p>
 * The command may print the estimate total travel time.
 */
public class Route extends HyperMetroCommand {
    private static final SearchAlgorithm<StationId> NO_ALGORITHM = (graph, source, target) -> List.of();

    private SearchAlgorithm<StationId> algorithm;
    private int transferTime;
    private boolean hideTime;

    public Route(final MetroService metroService) {
        super(metroService);
        this.algorithm = NO_ALGORITHM;
        this.transferTime = 0;
        this.hideTime = true;
    }

    @Override
    public String apply(List<String> parameters) {
        validateParametersNumber(parameters, REQUIRED_FOUR);
        final var source = new StationId(parameters.get(SOURCE_LINE), parameters.get(SOURCE_NAME));
        final var target = new StationId(parameters.get(TARGET_LINE), parameters.get(TARGET_NAME));

        final var graph = metroService.getMetroGraph(transferTime);
        final var route = algorithm.findPath(graph, source, target);
        final var timeMessage = hideTime ? "" : System.lineSeparator() +
                "Total: " + (int) graph.getDistance(route) + " minutes in the way";

        return printRoute(route) + timeMessage;
    }

    String printRoute(final List<StationId> route) {
        final var stringJoiner = new StringJoiner(System.lineSeparator());
        var line = route.get(0).line();

        for (final var node : route) {
            if (!node.line().equals(line)) {
                line = node.line();
                stringJoiner.add("Transition to line " + line);
            }
            stringJoiner.add(node.name());
            LOGGER.log(DEBUG, "MetroStation: {0}", node.name());
        }
        return stringJoiner.toString();
    }

    public SearchAlgorithm<StationId> getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SearchAlgorithm<StationId> algorithm) {
        this.algorithm = algorithm;
    }

    public int getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }

    public boolean isHideTime() {
        return hideTime;
    }

    public void setHideTime(boolean hideTime) {
        this.hideTime = hideTime;
    }
}
