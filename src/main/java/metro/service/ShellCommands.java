package metro.service;

import metro.HyperMetro;
import metro.algorithm.BreadthFirstSearch;
import metro.algorithm.DijkstrasAlgorithm;
import metro.algorithm.SearchAlgorithm;
import metro.model.MetroStation;
import metro.model.StationId;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

@ShellComponent
public record ShellCommands(MetroService metroService) {
    private static final System.Logger LOGGER = System.getLogger(HyperMetro.class.getName());
    private static final String PREFIX_PREV = "<---| ";
    private static final String PREFIX_NEXT = "--->| ";
    private static final String PREFIX_TRAN = "<---> ";
    private static final SearchAlgorithm<StationId> dijkstras = new DijkstrasAlgorithm<>();
    private static final SearchAlgorithm<StationId> bfsAlgorithm = new BreadthFirstSearch<>();

    @ShellMethod("Prints a metro line")
    public String output(String metroLine) {
        return metroService.getMetroLine(metroLine)
                .stations().stream()
                .map(this::printStation)
                .collect(joining(lineSeparator()));
    }

    private String printStation(final MetroStation metroStation) {
        final var name = metroStation.id().name();
        return name + lineSeparator()
                + printNeighbors(PREFIX_PREV, metroStation.prev())
                + printNeighbors(PREFIX_NEXT, metroStation.next())
                + printNeighbors(PREFIX_TRAN, metroStation.transfer());
    }

    private String printNeighbors(final String prefix, final Set<StationId> stations) {
        final Function<StationId, String> name = prefix.equals(PREFIX_TRAN) ? StationId::line : StationId::name;
        return stations.isEmpty() ? "" : stations.stream()
                .map(name.andThen(prefix::concat))
                .collect(joining(lineSeparator())) + lineSeparator();
    }

    @ShellMethod("Adds a new station at the beginning of the metro line")
    public String addHead(String line, String station) {
        metroService.addHead(line, station);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a new station at the end of the line")
    public String append(String line, String station) {
        metroService.append(line, station);
        return "Metro station successfully added";
    }

    @ShellMethod("Connects two metro stations")
    public String connect(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        metroService.connect(source, target);
        return "Metro stations successfully connected";
    }

    @ShellMethod("Removes a metro station")
    public String remove(String line, String station) {
        var source = new StationId(line, station);
        metroService.remove(source);
        return "Metro station successfully removed";
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public String route(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = metroService.getMetroGraph(5);
        var route = bfsAlgorithm.findPath(graph, source, target);
        return printRoute(route);
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public String fastestRoute(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = metroService.getMetroGraph(5);
        var route = dijkstras.findPath(graph, source, target);
        var timeMessage = System.lineSeparator() + "Total: " + (int) graph.getDistance(route) + " minutes in the way";
        return printRoute(route) + timeMessage;
    }

    private String printRoute(final List<StationId> route) {
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

}
