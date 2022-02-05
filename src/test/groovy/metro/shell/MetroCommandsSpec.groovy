package metro.shell

import metro.algorithm.Graph
import metro.algorithm.SearchAlgorithm
import metro.model.Station
import metro.model.StationId
import metro.repository.MetroRepository
import org.springframework.shell.table.BorderStyle
import org.springframework.shell.table.Table
import spock.lang.Specification
import spock.lang.Subject

class MetroCommandsSpec extends Specification {

    def graph = Mock Graph

    def repository = Mock MetroRepository
    def bfsAlgorithm = Mock SearchAlgorithm
    def dijkstrasAlgorithm = Mock SearchAlgorithm

    @Subject
    def commands = new MetroCommands(
            repository: repository,
            shortest: bfsAlgorithm,
            fastest: dijkstrasAlgorithm
    )

    void setup() {
        commands.borderStyle = BorderStyle.air.name()
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
        when: 'we execute route command'
        def output = commands.route sourceLine, sourceStation, targetLine, targetStation

        then: 'the command request the repository for a graph of metro'
        1 * repository.getGraph() >> graph

        and: 'the algorithm is called to find the shortest path'
        1 * bfsAlgorithm.findPath(graph, source, target) >> route

        and: 'the distance for the found path is calculated.'
        1 * graph.getDistance(route)

        and: 'we receive a report in the form of a table'
        output.getClass() == Table.class

        where: 'the parameters for fastest-route method are'
        sourceLine = 'L1'
        sourceStation = 'A1'
        targetLine = 'L2'
        targetStation = 'B2'

        and: 'source and target stationIds are'
        source = new StationId(sourceLine, sourceStation)
        target = new StationId(targetLine, targetStation)

        and: 'the route between stations is'
        route = [source, target]
    }

    def 'should execute fastest-route command'() {
        when: 'we execute fastest-route command'
        def output = commands.fastestRoute sourceLine, sourceStation, targetLine, targetStation

        then: 'the repository requested for a graph and it returns the mocked graph '
        1 * repository.getGraph() >> graph

        and: 'the algorithm is called to find the fastest path'
        1 * dijkstrasAlgorithm.findPath(graph, source, target) >> route

        and: 'the distance for the found path is calculated.'
        1 * graph.getDistance(route)

        and: 'we receive a report in the form of a table'
        output.getClass() == Table.class


        where: 'the parameters for fastest-route method are'
        sourceLine = 'L1'
        sourceStation = 'A1'
        targetLine = 'L2'
        targetStation = 'B2'

        and: 'source and target stationIds are'
        source = new StationId(sourceLine, sourceStation)
        target = new StationId(targetLine, targetStation)

        and: 'the route between stations is'
        route = [source, target]
    }

    def "should print metro lines"() {
        when: 'we execute the command'
        def output = commands.lines()

        then: 'the repository requested for the metro schema'
        1 * repository.getSchema() >> [:]

        and: 'the resulting table has four columns'
        output.getModel().columnCount == 4
    }

    def 'should output metro line by name'() {
        when: 'we execute the command output'
        def output = commands.output(metroLine)

        then: 'the repository requested for a metro line'
        1 * repository.findLine(metroLine) >> new ArrayDeque() {
            {
                add(new Station('Waterloo', 5))
            }
        }

        and: 'the table has four columns and two rows (header and one station)'
        with(output.getModel()) {
            columnCount == 4
            rowCount == 2
            getValue(1, 0) == 'Waterloo'
            getValue(1, 1).toString().isEmpty()
            getValue(1, 2).toString().isEmpty()
            getValue(1, 3).toString().isEmpty()
        }

        where:
        metroLine = 'Bakerloo line'
    }
}
