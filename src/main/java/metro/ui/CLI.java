package metro.ui;

import lombok.AllArgsConstructor;
import metro.controller.Broker;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@AllArgsConstructor
public class CLI implements Runnable {
    private final UserInterface ui;
    private final Predicate<String> exit;
    private final Broker broker;

    @Override
    public void run() {
        ui.write("Welcome to HyperMetro system!");
        Stream.generate(ui::read)
                .takeWhile(not(exit))
                .map(broker)
                .forEach(ui::write);
    }
}
