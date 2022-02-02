package metro.shell

import metro.algorithm.Graph
import metro.algorithm.SearchAlgorithm
import metro.model.StationId
import metro.repository.MetroRepository
import org.springframework.shell.table.Table
import spock.lang.Specification
import spock.lang.Subject

class MetroCommandsSpec extends Specification {

    def repository = Mock MetroRepository

    def tableReport = Mock Table

    @Subject
    def commands = Spy(MetroCommands) {
        getRouteTable(_ as List<StationId>) >> tableReport
    }

    void setup() {
        commands.setRepository(repository)
    }

    void cleanup() {
    }

    def "should execute add-head command"() {
        given: 'Metro line, station and transfer time to the next station'
        def metroLine = 'Bakerloo'
        def metroStation = 'Waterloo'
        def transferTime = 5

        when: 'we execute the command with these parameters'
        def output = commands.addHead(metroLine, metroStation, transferTime)

        then: 'the metro repository is called with the parameters'
        1 * repository.addHead(metroLine, metroStation, transferTime)

        and: 'execution of the command is successful'
        output.contains 'successfully'
    }

    def 'should execute append command'() {
        when: 'we execute the command with these parameters'
        def output = commands.append metroLine, metroStation, transferTime

        then: 'the metro repository is called with the parameters'
        1 * repository.append(metroLine, metroStation, transferTime)

        and: 'execution of the command is successful'
        output.contains 'successfully'

        where: 'metro line, station and transfer time to the next station as'
        metroLine = 'Bakerloo'
        metroStation = 'Waterloo'
        transferTime = 5

    }

    def 'should execute connect command'() {
        when: 'we execute the command with these parameters'
        def output = commands.connect 'one', 'A1', 'two', 'B2'

        then: 'the metro repository is called with the parameters'
        1 * repository.connect('one', 'A1', 'two', 'B2')

        and: 'execution of the command is successful'
        output.contains 'successfully'
    }

    def 'should execute remove command'() {
        when: 'we execute the command with these parameters'
        def output = commands.remove 'Bakerloo', 'Waterloo'

        then: 'the metro repository is called with the parameters'
        1 * repository.remove('Bakerloo', 'Waterloo')

        and: 'execution of the command is successful'
        output.contains 'successfully'
    }

    def 'should execute route command'() {
        given: 'we have some path finding algorithm'
        def algorithm = Mock SearchAlgorithm

        and: 'we set the algorithm to find the shortest path'
        commands.shortest = algorithm

        when: 'we execute route command'
        def output = commands.route 'L1', 'A1', 'L2', 'B2'

        then: 'the command request the repository for a graph of metro'
        1 * repository.getGraph()

        and: 'the algorithm is called to find the shortest path'
        1 * algorithm.findPath(_, _, _) >> []

        and: 'we receive a report in the form of a table'
        output === tableReport
    }

    def 'should execute fastest-route command'() {
        given: 'we have some path finding algorithm'
        def algorithm = Mock SearchAlgorithm

        and: 'we set the algorithm to find the fastest path'
        commands.fastest = algorithm

        and: 'we mock the graph for metro map'
        def graph = Mock Graph

        when: 'we execute fastest-route command'
        commands.fastestRoute 'L1', 'A1', 'L2', 'B2'

        then: 'the repository requested for a graph and it returns the mocked graph '
        1 * repository.getGraph() >> graph

        and: 'the algorithm is called to find the fastest path'
        1 * algorithm.findPath(_, _, _) >> []

        and: 'the distance for the found path is calculated.'
        1 * graph.getDistance(_)

    }


    def "Output"() {
    }
}
