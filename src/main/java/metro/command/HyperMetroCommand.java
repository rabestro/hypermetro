package metro.command;

import lombok.AllArgsConstructor;
import metro.service.MetroService;

import java.util.List;

@AllArgsConstructor
abstract class HyperMetroCommand implements Command {
    static final System.Logger LOGGER = System.getLogger("");

    static final int TRANSFER_TIME = 5;
    static final int REQUIRED_ONE = 1;
    static final int REQUIRED_TWO = 2;
    static final int REQUIRED_FOUR = 4;

    static final int SOURCE_LINE = 0;
    static final int SOURCE_NAME = 1;
    static final int TARGET_LINE = 2;
    static final int TARGET_NAME = 3;

    final MetroService metroService;

    void validateParametersNumber(final List<String> parameters, final int requiredNumber) {
        if (parameters.size() != requiredNumber) {
            throw new IllegalArgumentException("Invalid number of parameters.");
        }
    }

}
