package metro.controller

import metro.command.Command
import spock.lang.*

@Title("Commands Broker")
@Narrative("The broker parse the user input and execute appropriate commands")
@Issue("https://hyperskill.org/projects/120/stages/649/implement")
class BrokerSpec extends Specification {
    def commandOne = Mock Command
    def commandTwo = Mock Command
    def actions = [one: (commandOne), two: (commandTwo)]

    @Subject
    def broker = new Broker(actions)

    def 'should execute correct command'() {
        when:
        broker.apply('one Bakerloo Waterloo')

        then:
        1 * commandOne.apply(['Bakerloo', 'Waterloo'])
    }

    def 'should reject invalid command'() {
        given:
        def request = 'unknown Bakerloo Waterloo'

        when:
        def result = broker.apply(request)

        then:
        0 * commandOne.apply(_)

        and:
        result =~ /(?i)invalid command/
    }

    def 'should print error messages'() {
        given:
        def request = 'two Bakerloo Waterloo'

        when:
        def result = broker.apply(request)

        then:
        1 * commandTwo.apply(_) >> { throw exception }

        and:
        result =~ message

        where:
        message                        | exception
        'Invalid number of parameters' | new IllegalArgumentException(message)
        'No such metro line'           | new NoSuchElementException(message)
        'No such metro station'        | new NoSuchElementException(message)
    }

    @PendingFeature
    def 'should reject an empty command'() {
        given:
        def request = ''

        when:
        def result = broker.apply(request)

        then:
        0 * commandOne.apply(_)

        and:
        result =~ /(?i)invalid command/
    }
}
