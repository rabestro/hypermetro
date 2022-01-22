package metro.shell;

import metro.HyperMetro;
import metro.algorithm.SearchAlgorithm;
import metro.model.StationId;
import metro.printer.PrinterService;
import metro.service.MetroService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public record Commands(
        MetroService metroService,
        PrinterService printer,
        SearchAlgorithm<StationId> fastestAlgorithm,
        SearchAlgorithm<StationId> shortestAlgorithm
) {
    private static final System.Logger LOGGER = System.getLogger(HyperMetro.class.getName());

    @ShellMethod("Prints a metro line")
    public String output(String metroLine) {
        return printer().printLine(metroService.getMetroLine(metroLine));
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
        var graph = metroService.getMetroGraph();
        var route = shortestAlgorithm.findPath(graph, source, target);
        return printer().printRoute(route);
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public String fastestRoute(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = metroService.getMetroGraph();
        var route = fastestAlgorithm.findPath(graph, source, target);
        var timeMessage = System.lineSeparator() + "Total: " + (int) graph.getDistance(route) + " minutes in the way";
        return printer().printRoute(route) + timeMessage;
    }

}
