package metro.command

import metro.service.MetroService
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@Title("Abstract HyperMetro Command")
@Narrative("Contains method to validate number of parameters")
class HyperMetroCommandSpec extends Specification {
    def service = Mock MetroService

    def command = new HyperMetroCommand(service) {
        @Override
        String apply(List<String> strings) {
            return null
        }
    }

    @Unroll("required number #requiredNumber, parameters: #parameters")
    def 'should throw an exception for incorrect number of parameters'() {
        when:
        command.validateParametersNumber(parameters, requiredNumber)

        then:
        thrown IllegalArgumentException

        where:
        parameters      | requiredNumber
        []              | 2
        ['A']           | 2
        ['A', 'B', 'C'] | 2
        ['A', 'B', 'C'] | 4
        ['A']           | 4
        ['A', 'B']      | 4
    }

    @Unroll("required number #requiredNumber, parameters: #parameters")
    def 'should not throw an exception for correct number of parameters'() {
        when:
        command.validateParametersNumber(parameters, requiredNumber)

        then:
        noExceptionThrown()

        where:
        parameters           | requiredNumber
        []                   | 0
        ['A', 'B']           | 2
        ['A', 'B', 'C']      | 3
        ['A', 'B', 'C', 'D'] | 4

    }


}
