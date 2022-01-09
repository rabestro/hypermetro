package metro.repository;

import metro.model.MetroLine;
import metro.model.MetroStation;
import metro.model.StationId;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public interface MetroRepository {
    Optional<MetroLine> getLine(String name);

    Optional<MetroStation> getStation(StationId id);

    Stream<StationId> stream();

    void load(String fileName) throws IOException;

}
