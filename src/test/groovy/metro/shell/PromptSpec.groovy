package metro.shell

import metro.repository.MetroRepository
import spock.lang.Specification

class PromptSpec extends Specification {
    def repository = Mock MetroRepository

    def "should generate a prompt with metro name"() {
        given: 'a prompt provider for the shell'
        def prompt = new Prompt(repository)

        when: 'we get a prompt for the shell'
        def output = prompt.prompt

        then: 'the prompt provider asks the repository for the metro name'
        1 * repository.metroName() >> 'london'

        and: 'the prompt contains the name of metro'
        output.contains 'london'
    }
}
