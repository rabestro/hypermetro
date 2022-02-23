package metro.model;

import javax.validation.constraints.NotBlank;

/**
 * Subway station ID.
 *
 * @param line    the name of the subway line.
 * @param station the name of the subway station.
 */
public record StationId(@NotBlank String line, @NotBlank String station) {
}
