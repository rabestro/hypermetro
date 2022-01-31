package metro.algorithm;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.util.function.Predicate.not;

/**
 * Algorithm for finding the shortest paths between nodes in a graph.
 * <p>
 * The algorithm doesn't take into account the distance between nodes.
 *
 * @param <T> the type of vertex
 * @author Jegors Čemisovs
 * @since 1.0
 */
@Component("shortestAlgorithm")
public class BreadthFirstSearch<T> implements SearchAlgorithm<T> {

    @Override
    public List<T> findPath(Graph<T> graph, T source, T target) {
        var queue = new ArrayDeque<T>();
        var visited = new HashSet<T>();
        var previous = new HashMap<T, T>();
        queue.add(source);

        while (!queue.isEmpty()) {
            var node = queue.removeFirst();
            if (target.equals(node)) {
                return buildPath(target, previous);
            }
            visited.add(node);
            graph.edges(node).keySet().stream()
                    .filter(not(visited::contains))
                    .forEach(it -> {
                        previous.computeIfAbsent(it, x -> node);
                        queue.addLast(it);
                    });
        }
        return List.of();
    }

}