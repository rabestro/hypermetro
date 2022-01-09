package metro;

import lombok.AllArgsConstructor;
import metro.repository.MetroRepository;
import metro.ui.UserInterface;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

@AllArgsConstructor
public class Application {

    private final UserInterface ui;
    private final MetroRepository repository;
    private final Runnable commandLineInterface;

    public void start(final String fileName) {
        try {
            ui.write("Loading metro map from file: " + fileName);
            repository.load(fileName);
            commandLineInterface.run();
        } catch (NoSuchFileException exception) {
            ui.write("Error! Such a file doesn't exist!");
            exception.printStackTrace();
        } catch (IOException exception) {
            ui.write("Error loading a metro map file: " + exception.getMessage());
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
