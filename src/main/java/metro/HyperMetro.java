package metro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.Logger.Level.INFO;

@SpringBootApplication
public class HyperMetro {
    private static final System.Logger LOGGER = System.getLogger(HyperMetro.class.getName());

    public static void main(String[] args) {
        LOGGER.log(INFO, "Starting the application");

        if (args.length > 0 && !args[0].startsWith("--")) {
            LOGGER.log(INFO, "set property hypermetro.file={0}", args[0]);
            System.setProperty("hypermetro.file", args[0]);
        }

        SpringApplication.run(HyperMetro.class, args);
        LOGGER.log(INFO, "Application finished");
    }

}
