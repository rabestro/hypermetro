package metro.algorithm

import spock.lang.*

@Title("Breadth First Search Algorithm")
@See("https://hyperskill.org/learn/step/7068")
@Issue("https://hyperskill.org/projects/120/stages/651/implement")
@Narrative("""
Breadth First Search algorithm for finding the shortest paths between nodes in a graph
""")
class BreadthFirstSearchSpec extends Specification {
    @Subject
    def algorithm = new BreadthFirstSearch<String>()

    def 'should find the shortest path for a simple graph'() {
        given: 'a simple graph with three nodes'
        def graph = new Graph([
                A: [B: 7, C: 2],
                B: [A: 3, C: 5],
                C: [A: 1, B: 3]
        ])

        when: 'we use Breadth First Search algorithm to find the path'
        def path = algorithm.findPath(graph, source, target)

        then: 'we get the shortest path'
        path == shortest

        where:
        source | target || shortest
        'A'    | 'A'    || ['A']
        'A'    | 'B'    || ['A', 'B']
        'A'    | 'C'    || ['A', 'C']
        'B'    | 'C'    || ['B', 'C']
        'C'    | 'B'    || ['C', 'B']
    }

    def 'should find the shortest path for a complex graph'() {
        given: 'a complex graph with eighth nodes'
        def graph = new Graph([
                A: [B: 5, H: 2],
                B: [A: 5, C: 7],
                C: [B: 7, D: 3, G: 4],
                D: [C: 20, E: 4],
                E: [F: 5],
                F: [G: 6],
                G: [C: 4],
                H: [G: 3]
        ])

        when: 'we use Breadth First Search algorithm to find the path'
        def path = algorithm.findPath(graph, source, target)

        then: 'we get the shortest path'
        path == shortest

        where:
        source | target || shortest
        'A'    | 'A'    || ['A']
        'B'    | 'B'    || ['B']
        'A'    | 'B'    || ['A', 'B']
        'B'    | 'A'    || ['B', 'A']
        'A'    | 'C'    || ['A', 'B', 'C']
        'C'    | 'A'    || ['C', 'B', 'A']
        'A'    | 'G'    || ['A', 'H', 'G']
        'C'    | 'D'    || ['C', 'D']
        'D'    | 'C'    || ['D', 'C']
        'B'    | 'D'    || ['B', 'C', 'D']
        'D'    | 'B'    || ['D', 'C', 'B']
        'D'    | 'H'    || ['D', 'C', 'B', 'A', 'H']
    }

}
