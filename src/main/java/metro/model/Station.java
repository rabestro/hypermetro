package metro.model;

import java.util.Set;

/**
 * Subway station model.
 *
 * Includes the name of the station, a list of next and previous stations,
 * and a list of stations to which a transfer is possible.
 * 
 * @param name the name of the subway station.
 * @param time the travel time in minutest to the next station
 * @param next the set of next stations
 * @param prev the set of previous stations
 * @param transfer the set stationId connected to this station
 */
public record Station(String name, int time, Set<String> next, Set<String> prev, Set<StationId> transfer) {
}
