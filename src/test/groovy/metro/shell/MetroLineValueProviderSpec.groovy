package metro.shell


import metro.repository.MetroRepository
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import spock.lang.Specification
import spock.lang.Subject

class MetroLineValueProviderSpec extends Specification {
    def repository = Mock MetroRepository
    def context = Mock CompletionContext

    @Subject
    def valueProvider = new MetroLineValueProvider(repository)

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

    def "Complete"() {
    }
}
