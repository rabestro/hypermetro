package metro.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Set;

/**
 * Subway station model.
 * <p>
 * Includes the name of the station, a sets of next and previous stations,
 * and a set of stations to which a transfer is possible.
 *
 * @param name     the name of the subway station.
 * @param time     the travel time in minutes to the next station on the line
 * @param next     the set of next stations
 * @param prev     the set of previous stations
 * @param transfer the set of stations on other lines connected to this station
 */
public record Station(@NotBlank String name,
                      @PositiveOrZero int time,
                      @NotNull Set<String> next,
                      @NotNull Set<String> prev,
                      @NotNull Set<StationId> transfer) {
    /**
     * Creates a subway station model without references to other stations.
     *
     * @param name the name of the subway station.
     * @param time the travel time in minutest to the next station
     */
    public Station(@NotBlank String name, @PositiveOrZero int time) {
        this(name, time, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }
}
