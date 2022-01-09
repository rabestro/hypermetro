package metro.algorithm;

import java.util.List;

public interface SearchAlgorithm<T> {
    List<T> findPath(Graph<T> graph, T source, T target);
}
