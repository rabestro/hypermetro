package metro.ui;

import java.util.Scanner;

import static java.lang.System.Logger.Level.INFO;

@SuppressWarnings("squid:S106")
public class ConsoleInterface implements UserInterface {
    private static final System.Logger LOGGER = System.getLogger("HyperMetro");
    private static final Scanner scanner = new Scanner(System.in);

    private final boolean hasConsole = System.console() != null;

    {
        LOGGER.log(INFO, "Console: {0}", hasConsole);
    }

    @Override
    public String read() {
        System.out.print("> ");
        return hasConsole ? System.console().readLine() : scanner.nextLine();
    }

    @Override
    public void write(final Object line) {
        if (hasConsole) {
            System.console().writer().println(line);
        } else {
            System.out.println(line);
        }
    }
}
