package metro.command

import metro.model.StationId
import metro.service.MetroService
import spock.lang.*

@Title("Command: /connect")
@Narrative('''
Connect the stations using the command /connect "[line 1]" "[station 1]" "[line 2]" "[station 2]"
''')
@Issue("https://hyperskill.org/projects/120/stages/650/implement")
class ConnectSpec extends Specification {
    static final SOURCE_LINE = 'Edgington'
    static final SOURCE_STATION = 'Kennington'
    static final TARGET_LINE = 'Central'
    static final TARGET_STATION = 'Hanger Lane'
    static final SOURCE = new StationId(SOURCE_LINE, SOURCE_STATION)
    static final TARGET = new StationId(TARGET_LINE, TARGET_STATION)

    def service = Mock MetroService

    @Subject
    def command = new Connect(service)

    void setup() {
    }

    def "should execute connect command"() {
        given:
        def parameters = [SOURCE_LINE, SOURCE_STATION, TARGET_LINE, TARGET_STATION]

        when:
        def result = command.apply(parameters)

        then:
        1 * service.connect(SOURCE, TARGET)

        and:
        result.contains 'successfully'
    }

    def 'should throw an exception for incorrect number of parameters'() {
        when:
        command.apply(incorrectParameters)

        then:
        thrown IllegalArgumentException

        where:
        incorrectParameters << [[], ['Bakerloo'], ['A', 'B', 'C'], ['A', 'B', 'C', 'D', 'E']]
    }

}
