package metro.algorithm;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;

/**
 * Algorithm for finding the fastest paths between nodes in a graph.
 * <p>
 * The algorithm uses information about edge's distance to find the fastest path.
 *
 * @param <T> the type of vertex
 * @author Jegors Čemisovs
 * @since 1.0
 */
@Component("fastestAlgorithm")
public class DijkstrasAlgorithm<T> implements SearchAlgorithm<T> {

    @Override
    public List<T> findPath(Graph<T> graph, T source, T target) {
        var queue = new ArrayDeque<T>();
        var distances = new HashMap<T, Double>();
        var previous = new HashMap<T, T>();
        queue.add(source);
        distances.put(source, .0);

        while (!queue.isEmpty()) {
            var prev = queue.removeFirst();
            graph.edges(prev).forEach((node, time) -> {
                var distance = distances.get(prev) + time.doubleValue();
                if (distance < distances.getOrDefault(node, Double.MAX_VALUE)) {
                    previous.put(node, prev);
                    distances.put(node, distance);
                    queue.addLast(node);
                }
            });
        }
        var isFoundPath = previous.containsKey(target) || source.equals(target);

        return isFoundPath ? buildPath(target, previous) : List.of();
    }

}