package metro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.Logger.Level.INFO;

@SpringBootApplication
public class HyperMetro {
    private static final System.Logger LOGGER = System.getLogger(HyperMetro.class.getName());

    public static void main(String[] args) {
        LOGGER.log(INFO, "STARTING THE APPLICATION");
        SpringApplication.run(HyperMetro.class, args);
        LOGGER.log(INFO, "APPLICATION FINISHED");
    }
}
