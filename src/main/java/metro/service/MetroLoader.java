package metro.service;

import metro.repository.MetroRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

@Service
@Order(InteractiveShellApplicationRunner.PRECEDENCE - 200)
public record MetroLoader(MetroRepository repository) implements ApplicationRunner {
    private static final System.Logger LOGGER = System.getLogger(MetroLoader.class.getName());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getSourceArgs().length < 1) {
            throw new IllegalArgumentException(
                    "a filename with metro map should be specified as the application parameter");
        }
        var fileName = args.getSourceArgs()[0];
        try {
            LOGGER.log(INFO, "Loading metro map from file: {0}", fileName);
            repository.load(fileName);
        } catch (NoSuchFileException exception) {
            LOGGER.log(ERROR, "Error! Such a file doesn't exist!");
            throw exception;
        } catch (IOException exception) {
            LOGGER.log(ERROR, "Error loading a metro map file: {0}", exception.getMessage());
            throw exception;
        }

    }
}
