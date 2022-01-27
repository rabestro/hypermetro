package metro.shell;

import metro.repository.RepositoryMetro;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public record MetroCommands(RepositoryMetro repository) {
    private static final System.Logger LOGGER = System.getLogger(MetroCommands.class.getName());

    @ShellMethod("Adds a new station at the beginning of the metro line")
    public String addHead(String line, String station, int time) {
        repository.addHead(line, station, time);
        return "Metro station successfully added";
    }

    @ShellMethod("Prints a metro line")
    public void print(String line) {
        repository.findLine(line).forEach(System.out::println);
    }

    @ShellMethod("Adds a new station at the end of the line")
    public String append(String line, String station) {
        repository.append(line, station);
        return "Metro station successfully added";
    }

    @ShellMethod("Adds a transfer connection between two metro stations")
    public String connect(String sourceLine, String sourceStation, String targetLine, String targetStation) {
        repository.connect(sourceLine, sourceStation, targetLine, targetStation);
        return "Metro stations successfully connected";
    }

}
