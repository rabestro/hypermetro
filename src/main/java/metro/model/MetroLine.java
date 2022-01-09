package metro.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

public record MetroLine(String name, Deque<MetroStation> stations) {
    private static final System.Logger LOGGER = System.getLogger("HyperMetro");

    public MetroLine(String name) {
        this(name, new LinkedList<>());
    }

    public Optional<MetroStation> getStation(final String name) {
        return stations.stream().filter(s -> s.id().name().equals(name)).findAny();
    }

    public void remove(final MetroStation station) {
        stations.remove(station);
    }

    public void addHead(final String name) {
        final var sid = new StationId(this.name, name);
        final var station = new MetroStation(sid);
        if (!stations().isEmpty()) {
            final var prevStation = stations().getFirst();
            prevStation.prev().add(sid);
            station.next().add(prevStation.id());
        }
        stations().addFirst(station);
    }

    public void add(final MetroStation metroStation) {
        stations.add(metroStation);
    }

    public void append(final String stationName) {
        final var sid = new StationId(name, stationName);
        append(new MetroStation(sid));
    }

    private void append(final MetroStation metroStation) {
        if (!stations.isEmpty()) {
            final var lastStation = stations.getLast();
            lastStation.next().add(metroStation.id());
            metroStation.prev().add(lastStation.id());
        }
        stations.add(metroStation);
    }

    public Stream<MetroStation> stream() {
        return stations.stream();
    }
}
