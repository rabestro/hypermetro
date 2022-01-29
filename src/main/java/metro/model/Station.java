package metro.model;

import java.util.Set;

/**
 * Subway station model.
 *
 * Includes the name of the station, a list of next and previous stations,
 * and a list of stations to which a transfer is possible.
 */
public record Station(String name, int time, Set<String> next, Set<String> prev, Set<StationId> transfer) {
}