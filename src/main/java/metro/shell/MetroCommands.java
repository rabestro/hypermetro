package metro.shell;

import metro.algorithm.SearchAlgorithm;
import metro.repository.RepositoryMetro;
import metro.repository.StationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.StringJoiner;

import static java.lang.System.Logger.Level.TRACE;

@ShellComponent
public class MetroCommands {
    private static final System.Logger LOGGER = System.getLogger(MetroCommands.class.getName());

    private RepositoryMetro repository;

    private SearchAlgorithm<StationId> shortest;

    private SearchAlgorithm<StationId> fastest;

    private ShellHelper shellHelper;

    @ShellMethod("Adds a new station at the beginning of the metro line")
    public String addHead(String line, String station, int time) {
        repository.addHead(line, station, time);
        return shellHelper.getSuccessMessage("Metro station successfully added");
    }

    @ShellMethod("Prints a metro line")
    public void print(String line) {
        repository.findLine(line).forEach(System.out::println);
    }

    @ShellMethod("Adds a new station at the end of the line")
    public String append(String line, String station, int time) {
        repository.append(line, station, time);
        return shellHelper.getSuccessMessage("Metro station successfully added");
    }

    @ShellMethod("Adds a transfer connection between two metro stations")
    public String connect(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        repository.connect(sourceLine, sourceStation, targetLine, targetStation);
        return shellHelper.getSuccessMessage("Metro stations successfully connected");
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public String route(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = repository.getGraph();
        var route = shortest.findPath(graph, source, target);
        return printRoute(route);
    }

    @ShellMethod("Finds and prints the fastest route between two metro stations")
    public String fastestRoute(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = repository.getGraph();
        var route = fastest.findPath(graph, source, target);
        var timeMessage = System.lineSeparator() + "Total: " + (int) graph.getDistance(route) + " minutes in the way";
        return printRoute(route) + shellHelper.getInfoMessage(timeMessage);
    }

    @ShellMethod("Removes a station from the metro map")
    public String remove(String line, String station) {
        repository.remove(line, station);
        return "Metro station successfully removed";
    }

    @ShellMethod("Prints a metro line")
    public String output(String line) {
//        return printLine(repository.findLine(line));
        return null;
    }

    public String printRoute(List<StationId> route) {
        LOGGER.log(TRACE, "prints route: {0}", route);
        final var stringJoiner = new StringJoiner(System.lineSeparator());
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
    public void setRepository(RepositoryMetro repository) {
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

    @Autowired
    public void setShellHelper(ShellHelper shellHelper) {
        this.shellHelper = shellHelper;
    }

}
