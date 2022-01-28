package metro.shell;

import metro.algorithm.SearchAlgorithm;
import metro.model.StationId;
import metro.repository.MetroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

@ShellComponent
public class MetroCommands {
    private static final System.Logger LOGGER = System.getLogger(MetroCommands.class.getName());

    private MetroRepository repository;

    private SearchAlgorithm<StationId> shortest;

    private SearchAlgorithm<StationId> fastest;

    @Value("${hypermetro.terminal.width:120}")
    private int terminalWidth;

    @ShellMethod("Adds a new station at the beginning of the metro line")
    public String addHead(
            @ShellOption(help = "Name of the metro line") String line,
            @ShellOption(help = "Name of the metro station") String station,
            @ShellOption(help = "Travel time to the next station in minutes") int time
    ) {
        repository.addHead(line, station, time);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a new station at the end of the line")
    public String append(
            @ShellOption(help = "Name of the metro line") String line,
            @ShellOption(help = "Name of the metro station") String station,
            @ShellOption(help = "Travel time to the next station in minutes") int time
    ) {
        repository.append(line, station, time);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a transfer connection between two metro stations")
    public String connect(
            @ShellOption(help = "Name of the first metro line") String sourceLine,
            @ShellOption(help = "Name of the first metro station") String sourceStation,
            @ShellOption(help = "Name of the second metro line") String targetLine,
            @ShellOption(help = "Name of the second metro station") String targetStation
    ) {
        repository.connect(sourceLine, sourceStation, targetLine, targetStation);
        return "Metro stations successfully connected";
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public String route(
            @ShellOption(help = "Name of the starting metro line") String sourceLine,
            @ShellOption(help = "Name of the starting metro station") String sourceStation,
            @ShellOption(help = "Name of the final metro line") String targetLine,
            @ShellOption(help = "Name of the final metro station") String targetStation
    ) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = repository.getGraph();
        var route = shortest.findPath(graph, source, target);
        return printRoute(route);
    }

    @ShellMethod("Finds and prints the fastest route between two metro stations")
    public String fastestRoute(
            @ShellOption(help = "Name of the starting metro line") String sourceLine,
            @ShellOption(help = "Name of the starting metro station") String sourceStation,
            @ShellOption(help = "Name of the final metro line") String targetLine,
            @ShellOption(help = "Name of the final metro station") String targetStation
    ) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = repository.getGraph();
        var route = fastest.findPath(graph, source, target);
        var timeMessage = lineSeparator() + "Total: " + (int) graph.getDistance(route) + " minutes in the way";
        return printRoute(route) + timeMessage;
    }

    @ShellMethod("Removes a station from the metro map")
    public String remove(
            @ShellOption(help = "Name of the metro line") String line,
            @ShellOption(help = "Name of the metro station") String station
    ) {
        repository.remove(line, station);
        return "Metro station successfully removed";
    }

    @ShellMethod("Prints a metro line")
    public String output(
            @ShellOption(help = "Name of the metro line") String line
    ) {
        var header = Stream.ofNullable(new Object[]{"Station", "Next", "Previous", "Transfer"});
        var stations = repository.findLine(line).stream().map(station -> new Object[]{
                station.name(),
                join(lineSeparator(), station.next()),
                join(lineSeparator(), station.prev()),
                station.transfer().stream().map(StationId::line).collect(joining(lineSeparator()))
        });
        var table = Stream.concat(header, stations).toArray(Object[][]::new);

        return new TableBuilder(new ArrayTableModel(table))
                .addFullBorder(BorderStyle.fancy_light)
                .build()
                .render(terminalWidth);
    }

    public String printRoute(List<StationId> route) {
        LOGGER.log(TRACE, "prints route: {0}", route);
        final var stringJoiner = new StringJoiner(lineSeparator());
        var line = route.get(0).line();

        for (final var node : route) {
            if (!node.line().equals(line)) {
                line = node.line();
                stringJoiner.add("Transition to line " + line);
            }
            stringJoiner.add(node.station());
            LOGGER.log(TRACE, "route metro station: {0}", node.station());
        }
        return stringJoiner.toString();
    }

    @Autowired
    public void setRepository(MetroRepository repository) {
        this.repository = repository;
    }

    @Autowired
    @Qualifier("shortestAlgorithm")
    public void setShortest(SearchAlgorithm<StationId> shortest) {
        this.shortest = shortest;
    }

    @Autowired
    @Qualifier("fastestAlgorithm")
    public void setFastest(SearchAlgorithm<StationId> fastest) {
        this.fastest = fastest;
    }

}
