package metro.model;

/**
 * Subway station ID.
 *
 * @param line the name of the subway line.
 * @param station the name of the subway station.
 */
public record StationId(String line, String station) {
}
