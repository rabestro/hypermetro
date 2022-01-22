package metro.service

import metro.shell.Commands
import spock.lang.Specification
import spock.lang.Subject

class ShellCommandsSpec extends Specification {
    def metroService = Mock MetroService

    @Subject
    def commands = new Commands(metroService)

    def "Output"() {
    }

    def "should execute add-head command"() {
        given:
        def metroLine = 'Bakerloo'
        and:
        def metroStation = 'Waterloo'

        when: 'we execute the command with these parameters'
        def output = commands.addHead(metroLine, metroStation)

        then: 'the metro service is called with these parameters'
        1 * metroService.addHead(metroLine, metroStation)

        and: 'execution of the command is successful'
        output.contains 'successfully'
    }

    def "Append"() {
    }

    def "Connect"() {
    }

    def "Remove"() {
    }

    def "Route"() {
    }

    def "FastestRoute"() {
    }
}
