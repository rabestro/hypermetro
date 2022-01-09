package metro.command

import metro.service.MetroService
import spock.lang.Issue
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Title("Command: /add-head")
@Narrative("add-head adds a new station at the beginning of the metro line")
@Issue("https://hyperskill.org/projects/120/stages/649/implement")
class AddHeadSpec extends Specification {
    def service = Mock MetroService

    def "should execute add-head command"() {
        given: 'command /add-head'
        def command = new AddHead(service)

        and: 'metro line and station name as parameters'
        def parameters = ['Bakerloo', 'Waterloo']

        when: 'we execute the command with these parameters'
        def result = command.apply(parameters)

        then: 'the appropriate service is called with these parameters'
        1 * service.addHead('Bakerloo', 'Waterloo')

        and: 'execution of the command is successful'
        result.contains 'successfully'
    }

    def 'should check number of parameters'() {
        given: 'command /add-head'
        def command = new AddHead(service)

        and: 'incorrect number of parameters'
        def params = ['Bakerloo']

        when: 'we execute the command with incorrect parameters'
        command.apply(params)

        then: 'the parameters are checked and exception thrown'
        thrown IllegalArgumentException
    }

}
