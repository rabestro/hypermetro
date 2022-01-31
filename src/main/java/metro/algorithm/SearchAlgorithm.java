package metro.algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A functional interface for graph search algorithm
 *
 * @param <T> the type of vertex
 * @author Jegors Čemisovs
 * @since 1.0
 */
@FunctionalInterface
public interface SearchAlgorithm<T> {
    /**
     * Find the path from the source node to the target
     *
     * @param graph  The graph in which we search for the path
     * @param source Search starting point identifier
     * @param target Search finish point identifier
     * @return Path found or empty list if path cannot be found
     */
    List<T> findPath(Graph<T> graph, T source, T target);

    /**
     * Builds the path from the source node to the target node
     *
     * @param target   node
     * @param previous references to the previous nodes in the path
     * @return path from the source node to the target node
     */
    default List<T> buildPath(T target, Map<T, T> previous) {
        var path = new LinkedList<T>();
        Stream.iterate(target, Objects::nonNull, previous::get).forEach(path::addFirst);
        return path;
    }
}