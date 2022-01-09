package metro

import metro.repository.MetroRepository
import metro.ui.UserInterface
import spock.lang.*

@Title("HyperMetro Application")
@Issue("https://hyperskill.org/projects/120/stages/648/implement")
class ApplicationSpec extends Specification {
    def ui = Mock UserInterface
    def repository = Mock MetroRepository
    def cli = Mock Runnable

    @Subject
    Application application

    void setup() {
        application = new Application(ui, repository, cli)
    }

    def "should load metro map and start CLI"() {
        given: 'metro schema in json file'
        def metroMap = 'london.json'

        when: 'we start the application'
        application.start(metroMap)

        then: 'the repository loads metro map'
        1 * repository.load(metroMap)

        and: 'the command line interface is started'
        1 * cli.run()
    }

    @Rollup
    @Ignore
    def "should print a message in case of error"() {
        given: 'some incorrect file name'
        def fileName = 'unknown.json'

        when: 'we start the application'
        application.start(fileName)

        then: 'the repository try to load the file and throw an exception'
        1 * repository.load(fileName) >> { throw new IOException(errorMessage) }

        and: 'the error message printed'
        1 * ui.write(errorMessage)

        and: 'the program stops and no more actions performed'
        0 * cli.run()

        where:
        errorMessage = 'Incorrect file'
    }
}
