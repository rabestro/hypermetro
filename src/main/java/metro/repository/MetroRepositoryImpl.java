package metro.repository;

import metro.model.MetroLine;
import metro.model.MetroMap;
import metro.model.MetroStation;
import metro.model.StationId;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class MetroRepositoryImpl implements MetroRepository {
    private final MapLoader mapLoader;

    private MetroMap metroMap;

    private String name;

    public MetroRepositoryImpl(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
    }

    @Override
    public Optional<MetroLine> getLine(final String name) {
        return Optional.ofNullable(metroMap.lines().get(name));
    }

    @Override
    public Optional<MetroStation> getStation(final StationId id) {
        return getLine(id.line()).flatMap(metroLine -> metroLine.getStation(id.name()));
    }

    @Override
    public Stream<StationId> stream() {
        return metroMap.lines().values().stream().flatMap(MetroLine::stream).map(MetroStation::id);
    }

    @Override
    public void load(final String fileName) throws IOException {
        metroMap = mapLoader.load(fileName);
        name = fileName.replaceFirst("(^[^.]+).*", "$1");
    }

    @Override
    public String metroName() {
        return name;
    }

}
