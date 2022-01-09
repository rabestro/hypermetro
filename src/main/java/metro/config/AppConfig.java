package metro.config;

import metro.Application;
import metro.algorithm.BreadthFirstSearch;
import metro.algorithm.DijkstrasAlgorithm;
import metro.algorithm.SearchAlgorithm;
import metro.command.*;
import metro.controller.Broker;
import metro.model.StationId;
import metro.repository.MapLoader;
import metro.repository.MetroRepository;
import metro.repository.MetroRepositoryImpl;
import metro.service.MetroService;
import metro.service.MetroServiceImpl;
import metro.ui.CLI;
import metro.ui.ConsoleInterface;
import metro.ui.UserInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Configuration
public class AppConfig {

    @Bean(name = "ui")
    public UserInterface getUi() {
        return new ConsoleInterface();
    }

    @Bean(name = "metroService")
    public MetroService getMetroService() {
        return new MetroServiceImpl(getRepository());
    }

    @Bean(name = "exit")
    public Predicate<String> exit() {
        return Set.of("/exit", "exit", "quit", "/quit")::contains;
    }

    @Bean(name = "dijkstrasAlgorithm")
    public SearchAlgorithm<StationId> dijkstrasAlgorithm() {
        return new DijkstrasAlgorithm<>();
    }

    @Bean(name = "commands")
    public Map<String, Command> getCommands() {
        final var route = new Route(getMetroService());
        route.setAlgorithm(new BreadthFirstSearch<>());
        route.setTransferTime(0);
        route.setHideTime(true);

        final var fastestRoute = new Route(getMetroService());
        fastestRoute.setAlgorithm(dijkstrasAlgorithm());
        fastestRoute.setTransferTime(5);
        fastestRoute.setHideTime(false);

        return Map.of(
                "output", new Output(getMetroService()),
                "print", new Print(getMetroService()),
                "append", new Append(getMetroService()),
                "add-head", new AddHead(getMetroService()),
                "connect", new Connect(getMetroService()),
                "remove", new Remove(getMetroService()),
                "route", route,
                "fastest-route", fastestRoute);
    }

    @Bean(name = "cli")
    public Runnable getCLI() {
        return new CLI(getUi(), exit(), getBroker());
    }

    @Bean(name = "repository")
    public MetroRepository getRepository() {
        return new MetroRepositoryImpl(new MapLoader());
    }

    @Bean(name = "broker")
    public Broker getBroker() {
        return new Broker(getCommands());
    }

    @Bean(name = "application")
    public Application getApplication() {
        return new Application(getUi(), getRepository(), getCLI());
    }

}
