package metro.ui;

import java.util.Scanner;

@SuppressWarnings("squid:S106")
public class ConsoleInterface implements UserInterface {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public String read() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    @Override
    public void write(final Object line) {
        System.out.println(line);
    }
}
