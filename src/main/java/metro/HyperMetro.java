package metro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.INFO;

@SpringBootApplication
public class HyperMetro {
    private static final Pattern JSON_FILE_NAME = Pattern.compile("\\w+\\.jso?n", Pattern.CASE_INSENSITIVE);
    private static final System.Logger LOGGER = System.getLogger(HyperMetro.class.getName());

    public static void main(String[] args) {
        LOGGER.log(INFO, "Starting the application");
        if (args.length == 1 && JSON_FILE_NAME.matcher(args[0]).matches()) {
            System.setProperty("hypermetro.file", args[0]);
        }
        SpringApplication.run(HyperMetro.class, args);
        LOGGER.log(INFO, "Application finished");
    }
}
