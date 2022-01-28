package metro.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

@ShellComponent
public class DemoCommands {

    public String[] CONTINENTS = {"Europe", "North America", "South America", "Africa", "Asia", "Austraila\n and Oceania"};
    public String[] COUNTRIES1 = {"Germany", "USA", "Brasil", "Nigeria", "China", "Australia"};
    public String[] COUNTRIES2 = {"France", "Canada", "Argentina", "Egypt", "India", "New Zeeland"};

    @Autowired
    ShellHelper shellHelper;

    @ShellMethod("Displays greeting message to the user whose name is supplied")
    public String echo(@ShellOption({"-N", "--name"}) String name) {
        String message = String.format("Hello %s!", name);
        shellHelper.print(message.concat(" (Default style message)"));
        shellHelper.printError(message.concat(" (Error style message)"));
        shellHelper.printWarning(message.concat(" (Warning style message)"));
        shellHelper.printInfo(message.concat(" (Info style message)"));
        shellHelper.printSuccess(message.concat(" (Success style message)"));

        String output = shellHelper.getSuccessMessage(message);
        return output.concat(" You are running spring shell cli-demo.");
    }

    @ShellMethod("Display sample tables")
    public void sampleTables() {
        Object[][] sampleData = new String[][]{
                CONTINENTS,
                COUNTRIES1,
                COUNTRIES2
        };
        TableModel model = new ArrayTableModel(sampleData);
        TableBuilder tableBuilder = new TableBuilder(model);

        shellHelper.printInfo("air border style");
        tableBuilder.addFullBorder(BorderStyle.air);
        shellHelper.print(tableBuilder.build().render(80));

        shellHelper.printInfo("oldschool border style");
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        shellHelper.print(tableBuilder.build().render(80));

        shellHelper.printInfo("fancy_light border style");
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        shellHelper.print(tableBuilder.build().render(80));

        shellHelper.printInfo("fancy_double border style");
        tableBuilder.addFullBorder(BorderStyle.fancy_double);
        shellHelper.print(tableBuilder.build().render(80));

        shellHelper.printInfo("mixed border style");
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
        shellHelper.print(tableBuilder.build().render(80));
    }
}
