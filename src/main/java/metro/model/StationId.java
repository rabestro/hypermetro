package metro.model;

import metro.validation.MetroStation;

/**
 * Subway station ID.
 *
 * @param line    the name of the subway line.
 * @param station the name of the subway station.
 */
public record StationId(String line, @MetroStation String station) {
}
