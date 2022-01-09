package metro.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import metro.model.MetroLine;
import metro.model.MetroMap;
import metro.model.MetroStation;
import metro.model.StationId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.System.Logger.Level.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class MapLoader {
    private static final System.Logger LOGGER = System.getLogger("HyperMetro");

    private static Set<StationId> parseStations(final String line, final JsonElement jsonElement) {
        LOGGER.log(TRACE, "Parse stations {0}", jsonElement);
        final var stations = new HashSet<StationId>();
        if (!jsonElement.isJsonNull()) {
            jsonElement.getAsJsonArray().forEach(element -> {
                final var name = element.getAsString();
                final var id = new StationId(line, name);
                stations.add(id);
            });
        }
        LOGGER.log(TRACE, "Stations: {0}", stations);
        return stations;
    }

    private static int getTime(final JsonObject jsonStation) {
        final var hasTime = jsonStation.has("time") && !jsonStation.get("time").isJsonNull();
        return hasTime ? jsonStation.get("time").getAsInt() : 1;
    }

    public MetroMap load(final String fileName) throws IOException {
        LOGGER.log(INFO, "Loading Metro from file: " + fileName);
        final var reader = Files.newBufferedReader(Paths.get(fileName));
        final var json = JsonParser.parseReader(reader);
        final var lines = json.getAsJsonObject()
                .entrySet().stream()
                .map(this::parseMetroLine)
                .collect(toUnmodifiableMap(MetroLine::name, identity()));
        LOGGER.log(INFO, "Loaded metro lines: " + lines.keySet());
        return new MetroMap(lines);
    }

    private MetroLine parseMetroLine(final Map.Entry<String, JsonElement> jsonLine) {
        final var lineName = jsonLine.getKey();
        final var metroLine = new MetroLine(lineName);
        LOGGER.log(DEBUG, "Import metro line: " + lineName);

        final var jsonStations = jsonLine.getValue().getAsJsonArray();
        jsonStations.forEach(station -> {
            final var jsonStation = station.getAsJsonObject();
            final var metroStation = parseMetroStation(lineName, jsonStation);
            metroLine.add(metroStation);
        });
        return metroLine;
    }

    private MetroStation parseMetroStation(final String line, final JsonObject jsonStation) {
        final var name = jsonStation.get("name").getAsString();
        LOGGER.log(TRACE, "Create station '" + name + "' (" + line + ")");
        final var time = getTime(jsonStation);
        final var id = new StationId(line, name);
        final var prevStations = parseStations(line, jsonStation.get("prev"));
        final var nextStations = parseStations(line, jsonStation.get("next"));
        final var tranStations = parseTransfer(jsonStation.get("transfer"));
        final var station = new MetroStation(id, time, nextStations, prevStations, tranStations);
        LOGGER.log(TRACE, station);
        return station;
    }

    private Set<StationId> parseTransfer(final JsonElement jsonElement) {
        final var transfer = new HashSet<StationId>();
        if (!jsonElement.isJsonNull()) {
            jsonElement.getAsJsonArray().forEach(element -> {
                final var jsonObject = element.getAsJsonObject();
                final var id = parseStationId(jsonObject);
                transfer.add(id);
            });
        }
        return transfer;
    }

    private StationId parseStationId(final JsonObject jsonObject) {
        return new StationId(
                jsonObject.get("line").getAsString(),
                jsonObject.get("station").getAsString()
        );
    }

}
