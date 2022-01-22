package metro.service;

import lombok.AllArgsConstructor;
import metro.algorithm.Graph;
import metro.model.MetroLine;
import metro.model.MetroStation;
import metro.model.StationId;
import metro.repository.MetroRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.System.Logger.Level.DEBUG;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Service
@AllArgsConstructor
public class MetroServiceImpl implements MetroService {
    private static final System.Logger LOG = System.getLogger("MetroService");
    private static final String NOT_FOUND = "No such metro station or metro line was found.";
    private static final Supplier<NoSuchElementException> NOT_FOUND_EXCEPTION = () -> new NoSuchElementException(NOT_FOUND);

    private final MetroRepository repository;

    @Override
    public MetroLine getMetroLine(final String name) {
        LOG.log(DEBUG, "gets metro line for name = [{0}]", name);
        return repository.getLine(name).orElseThrow(NOT_FOUND_EXCEPTION);
    }

    @Override
    public MetroStation getMetroStation(final StationId id) {
        LOG.log(DEBUG, "gets metro station for id = [{0}]", id);
        return repository.getStation(id).orElseThrow(NOT_FOUND_EXCEPTION);
    }

    @Override
    public void addHead(final String lineName, final String stationName) {
        LOG.log(DEBUG, "add-head station [{1}] to line [{0}]", lineName, stationName);
        getMetroLine(lineName).addHead(stationName);
    }

    @Override
    public void append(final String lineName, final String stationName) {
        LOG.log(DEBUG, "append station [{1}] to line [{0}]", lineName, stationName);
        getMetroLine(lineName).append(stationName);
    }

    @Override
    public void remove(final StationId target) {
        LOG.log(DEBUG, "remove station [{0}]", target);
        getMetroLine(target.line()).remove(getMetroStation(target));
    }

    @Override
    public void connect(final StationId source, final StationId target) {
        LOG.log(DEBUG, "connect station [{0}] to [{1}]", source, target);
        getMetroStation(source).transfer().add(target);
        getMetroStation(target).transfer().add(source);
    }

    @Override
    public Graph<StationId> getMetroGraph(final int transferTime) {
        LOG.log(DEBUG, "create graph with transfer time {1} min.", transferTime);
        final Function<StationId, Map<StationId, Number>> getEdges = id -> {
            final var edges = new HashMap<StationId, Number>();
            final var source = getMetroStation(id);
            source.next().forEach(target -> edges.put(target, source.time()));
            source.prev().forEach(target -> edges.put(target, getMetroStation(target).time()));
            source.transfer().forEach(target -> edges.put(target, transferTime));
            return edges;
        };
        final var schema = repository.stream().collect(toUnmodifiableMap(identity(), getEdges));
        return Graph.of(schema);
    }

}
