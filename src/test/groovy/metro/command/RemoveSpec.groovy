package metro.command

import metro.model.StationId
import metro.service.MetroService
import spock.lang.*

@Title("Command: /remove")
@Narrative("Remove a metro station")
@Issue("https://hyperskill.org/projects/120/stages/649/implement")
class RemoveSpec extends Specification {
    static final TARGET_LINE = 'Central'
    static final TARGET_STATION = 'Hanger Lane'
    static final TARGET = new StationId(TARGET_LINE, TARGET_STATION)

    def service = Mock MetroService

    @Subject
    def command = new Remove(service)

    void setup() {
    }

    def "should execute remove command"() {
        given:
        def parameters = [TARGET_LINE, TARGET_STATION]

        when:
        def result = command.apply(parameters)

        then:
        1 * service.remove(TARGET)

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
