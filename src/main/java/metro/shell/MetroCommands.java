package metro.shell;

import metro.algorithm.SearchAlgorithm;
import metro.model.Station;
import metro.model.StationId;
import metro.repository.MetroRepository;
import metro.validation.MetroLine;
import metro.validation.MetroStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.NoWrapSizeConstraints;
import org.springframework.shell.table.SizeConstraints;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.join;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static org.springframework.shell.table.CellMatchers.column;
import static org.springframework.shell.table.SimpleHorizontalAligner.right;

@ShellComponent
public class MetroCommands {
    private static final SizeConstraints NO_WRAP = new NoWrapSizeConstraints();

    private MetroRepository repository;

    private SearchAlgorithm<StationId> shortest;

    private SearchAlgorithm<StationId> fastest;

    @Value("${hypermetro.table.border:oldschool}")
    private String borderStyle;

    @ShellMethod("Adds a new station at the beginning of the metro line")
    public String addHead(
            @ShellOption(help = "Name of the metro line", valueProvider = LineValueProvider.class)
            @MetroLine String line,
            @ShellOption(help = "Name of the metro station")
            @MetroStation String station,
            @ShellOption(help = "Travel time to the next station in minutes")
            @Positive int time
    ) {
        repository.addHead(line, station, time);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a new station at the end of the line")
    public String append(
            @ShellOption(help = "Name of the metro line", valueProvider = LineValueProvider.class)
            @MetroLine String line,
            @ShellOption(help = "Name of the metro station")
            @MetroStation String station,
            @ShellOption(help = "Travel time to the next station in minutes")
            @Positive int time
    ) {
        repository.append(line, station, time);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a transfer connection between two metro stations")
    public String connect(
            @ShellOption(help = "Name of the first metro line", valueProvider = LineValueProvider.class)
            @MetroLine String sourceLine,
            @ShellOption(help = "Name of the first metro station", valueProvider = StationValueProvider.class)
            @MetroStation String sourceStation,
            @ShellOption(help = "Name of the second metro line", valueProvider = LineValueProvider.class)
            @MetroLine String targetLine,
            @ShellOption(help = "Name of the second metro station")
            @MetroStation String targetStation
    ) {
        repository.connect(sourceLine, sourceStation, targetLine, targetStation);
        return "Metro stations successfully connected";
    }

    @ShellMethod("Removes a station from the metro map")
    public String remove(
            @ShellOption(help = "Name of the metro line", valueProvider = LineValueProvider.class)
            @MetroLine String line,
            @ShellOption(help = "Name of the metro station", valueProvider = StationValueProvider.class)
            @MetroStation String station
    ) {
        repository.remove(line, station);
        return "Metro station successfully removed";
    }

    @ShellMethod("Finds and prints the shortest route between two metro stations")
    public Table route(
            @ShellOption(help = "Name of the starting metro line", valueProvider = LineValueProvider.class)
            @MetroLine String sourceLine,
            @ShellOption(help = "Name of the starting metro station", valueProvider = StationValueProvider.class)
            @MetroStation String sourceStation,
            @ShellOption(help = "Name of the final metro line", valueProvider = LineValueProvider.class)
            @MetroLine String targetLine,
            @ShellOption(help = "Name of the final metro station", valueProvider = StationValueProvider.class)
            @MetroStation String targetStation
    ) {
        return getRouteTable(shortest, sourceLine, sourceStation, targetLine, targetStation);
    }

    @ShellMethod("Finds and prints the fastest route between two metro stations")
    public Table fastestRoute(
            @ShellOption(help = "Name of the starting metro line", valueProvider = LineValueProvider.class)
            @MetroLine String sourceLine,
            @ShellOption(help = "Name of the starting metro station", valueProvider = StationValueProvider.class)
            @MetroStation String sourceStation,
            @ShellOption(help = "Name of the final metro line", valueProvider = LineValueProvider.class)
            @MetroLine String targetLine,
            @ShellOption(help = "Name of the final metro station", valueProvider = StationValueProvider.class)
            @MetroStation String targetStation
    ) {
        return getRouteTable(fastest, sourceLine, sourceStation, targetLine, targetStation);
    }

    private Table getRouteTable(
            SearchAlgorithm<StationId> algorithm,
            String sourceLine, String sourceStation,
            String targetLine, String targetStation) {
        var source = new StationId(sourceLine, sourceStation);
        var target = new StationId(targetLine, targetStation);
        var graph = repository.getGraph();
        var route = algorithm.findPath(graph, source, target);

        var line = route.get(0).line();
        var data = new ArrayList<Object[]>();
        data.add(new String[]{"", "Route"});
        var number = 1;
        for (final var node : route) {
            if (!node.line().equals(line)) {
                line = node.line();
                data.add(new String[]{"", "Transition to line " + line});
            }
            data.add(new Object[]{number++, node.station()});
        }
        data.add(new Object[]{"", ""});
        data.add(new Object[]{"Total", (int) graph.getDistance(route) + " minutes in the way"});

        var table = data.toArray(Object[][]::new);

        return new TableBuilder(new ArrayTableModel(table))
                .addHeaderAndVerticalsBorders(BorderStyle.air)
                .on(column(0)).addAligner(right)
                .on(column(1)).addSizer(NO_WRAP)
                .build();
    }

    @ShellMethod("Outputs all stations for a given metro line")
    public Table output(
            @ShellOption(help = "Name of the metro line", valueProvider = LineValueProvider.class)
            @MetroLine String line
    ) {
        var header = Stream.ofNullable(new Object[]{"Station", "Next", "Previous", "Transfer to line"});
        var stations = repository.findLine(line).stream().map(station -> new Object[]{
                station.name(),
                join(lineSeparator(), station.next()),
                join(lineSeparator(), station.prev()),
                station.transfer().stream().map(StationId::line).collect(joining(lineSeparator()))
        });
        var report = Stream.concat(header, stations).toArray(Object[][]::new);

        return new TableBuilder(new ArrayTableModel(report))
                .addFullBorder(BorderStyle.valueOf(borderStyle))
                .on(column(0)).addSizer(NO_WRAP)
                .on(column(1)).addSizer(NO_WRAP)
                .on(column(2)).addSizer(NO_WRAP)
                .on(column(3)).addSizer(NO_WRAP)
                .build();
    }

    @ShellMethod("Prints information about metro lines")
    public Table lines() {
        var header = Stream.ofNullable(new Object[]{"Metro Line", "Stations", "Length (min)", "Transfers"});
        var lines = repository.getSchema().entrySet().stream().map(entry -> new Object[]{
                entry.getKey(),
                entry.getValue().size(),
                entry.getValue().stream().mapToInt(Station::time).sum(),
                entry.getValue().stream().map(Station::transfer).mapToInt(Set::size).sum()
        });
        var report = Stream.concat(header, lines).toArray(Object[][]::new);

        return new TableBuilder(new ArrayTableModel(report))
                .addHeaderAndVerticalsBorders(BorderStyle.valueOf(borderStyle))
                .on(column(0)).addSizer(NO_WRAP)
                .on(column(1)).addAligner(right)
                .on(column(2)).addAligner(right).addSizer(NO_WRAP)
                .on(column(3)).addAligner(right)
                .build();
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
