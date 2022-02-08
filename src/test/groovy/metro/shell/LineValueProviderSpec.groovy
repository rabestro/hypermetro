package metro.shell

import metro.model.Station
import metro.repository.MetroRepository
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import spock.lang.Specification
import spock.lang.Subject

class LineValueProviderSpec extends Specification {
    def repository = Mock MetroRepository
    def context = Mock CompletionContext

    @Subject
    def valueProvider = new LineValueProvider(repository)

    def "should support only parameters of type String"() {
        given:
        def parameter = Stub(MethodParameter) {
            getParameterType() >> type
        }

        when:
        def result = valueProvider.supports(parameter, context)

        then:
        result == expected

        and:
        0 * context._

        where:
        type          | expected
        String.class  | true
        Integer.class | false
        Number.class  | false
        Double.class  | false
        Long.class    | false

    }

    def "should suggest metro lines"() {
        given: 'parameter is mocked and no hints'
        def hints = [] as String[]
        def parameter = Mock MethodParameter

        when: 'we call value provider to complete our word'
        def proposals = valueProvider.complete(parameter, context, hints)

        then: 'the context is called and the repository requested for the schema of metro'
        1 * context.currentWordUpToCursor() >> word
        1 * repository.getSchema() >> schema

        and: 'the value of proposals as expected'
        proposals*.value() == expected

        where: 'the metro schema as'
        schema = [
                Bakerloo: [new Station('Kenton', 5)],
                Circle  : [], District: [], Jubilee: [], Central: []
        ]

        and: 'entered word and expected suggestions are'
        word  | expected
        'B'   | ['Bakerloo']
        'C'   | ['Circle', 'Central']
        'Co'  | []
        'D'   | ['District']
        'Jub' | ['Jubilee']
        'F'   | []
    }
}
