package metro.shell

import metro.repository.MetroRepository
import spock.lang.Specification
import spock.lang.Subject

class MetroCommandsSpec extends Specification {

    def repository = Mock MetroRepository

    @Subject
    def commands = new MetroCommands(repository: repository)

    void setup() {

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

    def "Route"() {
    }

    def "FastestRoute"() {
    }

    def "Output"() {
    }
}
