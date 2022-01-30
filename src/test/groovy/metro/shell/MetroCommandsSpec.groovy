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

    def "should append a new metro station to metro line"() {

    }

    def "Connect"() {
    }

    def "Route"() {
    }

    def "FastestRoute"() {
    }

    def "Remove"() {
    }

    def "Output"() {
    }
}
