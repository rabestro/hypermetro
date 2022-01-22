package metro.service;

import metro.algorithm.Graph;
import metro.model.MetroLine;
import metro.model.MetroStation;
import metro.model.StationId;

public interface MetroService {
    /**
     * Gets MetroLine by name
     *
     * @param name is the name of Metro Line
     * @return object representing the Metro Line
     */
    MetroLine getMetroLine(String name);

    /**
     * Gets Metro MetroStation by MetroStation ID
     *
     * @param stationId is metro station id
     * @return object representing Metro MetroStation
     */
    MetroStation getMetroStation(StationId stationId);

    /**
     * Adds a new station at the beginning of metro line.
     *
     * @param lineName    is the name of Metro Line
     * @param stationName is the name of Metro MetroStation
     */
    void addHead(String lineName, String stationName);

    /**
     * Appends a new station at the end of the line.
     *
     * @param lineName    is the name of Metro Line
     * @param stationName is the name of Metro MetroStation
     */
    void append(String lineName, String stationName);

    /**
     * Connects the source metro station to the target station
     *
     * @param source metro station id
     * @param target metro station id
     */
    void connect(StationId source, StationId target);

    /**
     * Removes the metro station from metro map
     *
     * @param target metro station id
     */
    void remove(StationId target);

    /**
     * Creates an abstract Graph representing the metro schema
     *
     * @return the graph with nodes as metro station
     */
    Graph<StationId> getMetroGraph();

}
