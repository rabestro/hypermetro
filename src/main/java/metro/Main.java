package metro;

import metro.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static java.lang.System.Logger.Level.INFO;

public class Main {
    private static final System.Logger LOGGER = System.getLogger("HyperMetro");

    public static void main(String[] args) {
        final var appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        final var application = appContext.getBean("application", Application.class);
        final var metroMapJson = args.length == 0 ? "london.json" : args[0];

        LOGGER.log(INFO, "HyperMetro started");
        application.start(metroMapJson);
        LOGGER.log(INFO, "HyperMetro finished");
    }

}
