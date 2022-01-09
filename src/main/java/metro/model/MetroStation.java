package metro.model;

import java.util.HashSet;
import java.util.Set;

public record MetroStation(StationId id, int time, Set<StationId> next, Set<StationId> prev, Set<StationId> transfer) {
    public MetroStation(StationId id) {
        this(id, 1);
    }

    public MetroStation(StationId id, int time) {
        this(id, time, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }
}
