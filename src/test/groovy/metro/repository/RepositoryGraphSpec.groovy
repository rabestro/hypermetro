package metro.repository

import metro.model.Station
import metro.model.StationId
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Repository 'getGraph' specifications")
class RepositoryGraphSpec extends Specification {
    @Subject
    def repository = new MetroRepositoryJson()

    def 'should create an empty graph from the empty metro schema'() {
        given: 'an empty metro map'
        repository.metroMap = [:]

        when: 'we create a graph from the metro map'
        def graph = repository.getGraph()

        then: 'we get an empty graph'
        graph.schema().isEmpty()
    }

    def 'should create a graph from one line, one station metro schema'() {
        given: 'a one line, one station metro schema'
        repository.metroMap = [(line): [new Station(station, 5)] as ArrayDeque]

        when: 'we create a graph from the metro map'
        def graph = repository.getGraph()

        then: 'the graph schema is not empty'
        graph.schema()

        and: 'it has only one vertex'
        graph.schema().size() == 1

        and: 'the only vertex in the graph has correct Station ID'
        graph.schema().keySet().contains new StationId(line, station)

        where: 'metro line and metro station as'
        line            | station
        'Bakerloo line' | 'Waterloo'
        'Central line'  | 'White City'
    }

    def 'should create a graph from one line and three stations'() {
        given: 'three stations creating a open-ended line'
        def A1 = new Station('A1', 7, ['A2'] as Set, [] as Set, [] as Set)
        def A2 = new Station('A2', 5, ['A3'] as Set, ['A1'] as Set, [] as Set)
        def A3 = new Station('A3', 9, [] as Set, ['A2'] as Set, [] as Set)

        and: 'a metro line with these stations'
        def L1 = [A1, A2, A3] as ArrayDeque

        and: 'a metro schema with one line'
        repository.metroMap = [one: L1]

        when: 'we create a graph from the metro map'
        def graph = repository.getGraph()

        then: 'the graph schema is not empty and it has correct vertexes'
        with(graph.schema()) {
            !isEmpty()
            size() == 3
            keySet() == [S1, S2, S3] as Set
        }

        and: 'each vertex has correct edges'
        with(graph) {
            edges(S1) == [(S2): 7]
            edges(S2) == [(S1): 7, (S3): 5]
            edges(S3) == [(S2): 5]
        }

        where:
        S1 = new StationId('one', 'A1')
        S2 = new StationId('one', 'A2')
        S3 = new StationId('one', 'A3')
    }

}
