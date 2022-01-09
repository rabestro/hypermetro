package metro.model;

import java.util.Map;

public record MetroMap(Map<String, MetroLine> lines) {
}