package metro.repository;

import metro.algorithm.Graph;
import metro.model.Station;
import metro.model.StationId;

import java.util.Deque;
import java.util.Map;

public interface MetroRepository {
    /**
     * Finds a metro line by its name
     *
     * @param line - the name of metro line
     * @return list of metro stations for the given metro line.
     */
    Deque<Station> findLine(String line);

    /**
     * Adds a new station at the beginning of the metro line
     *
     * @param line    - the name of metro line
     * @param station - the name of new metro station
     * @param time    - travel time to the next metro station
     * @throws NullPointerException if there is no such metro line
     */
    void addHead(String line, String station, int time);

    /**
     * Adds a new station at the end of the metro line
     *
     * @param line    - the name of metro line
     * @param station - the name of new metro station
     * @throws java.util.NoSuchElementException if there is no such metro line
     */
    void append(String line, String station, int time);

    /**
     * Removes a station from the metro map.
     *
     * @param line-    the name of metro line
     * @param station- the name of metro station to remove
     */
    void remove(String line, String station);

    /**
     * Adds a transfer connection between two metro stations.
     *
     * @param sourceLine     - the name of the first metro line
     * @param sourceStation- the name of the first metro station
     * @param targetLine     - the name of the second metro line
     * @param targetStation- the name of the second metro station
     * @throws java.util.NoSuchElementException if there is no such metro line or station
     */
    void connect(String sourceLine, String sourceStation, String targetLine, String targetStation);

    /**
     * Creates a direct weighted graph based on the metro map.
     *
     * @return a direct weighted graph with StationId as vertexes
     */
    Graph<StationId> getGraph();

    /**
     * Returns metro schema
     *
     * @return the schema of the metro
     */
    Map<String, Deque<Station>> getSchema();

    /**
     * Returns name of the metro
     *
     * @return metro name
     */
    String getMetroName();
}
