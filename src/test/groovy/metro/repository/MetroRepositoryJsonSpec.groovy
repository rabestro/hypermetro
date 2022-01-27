package metro.repository

import io.github.joke.spockmockable.Mockable
import metro.model.Station
import spock.lang.Specification
import spock.lang.Subject

@Mockable(Station)
class MetroRepositoryJsonSpec extends Specification {
    def stationOne = Mock Station
    def stationTwo = Mock Station

    @Subject
    def repository = new MetroRepositoryJson()

    void setup() {
    }

    void cleanup() {
    }

    def "should find metro line by name"() {
        given: 'the repository with given metro map'
        repository.metroMap = [one: [stationOne, stationTwo] as Deque, two: [] as Deque]

        when: 'we request the repository for existing line'
        def line = repository.findLine('one')

        then: 'it returns expected metro line'
        line == [stationOne, stationTwo]

        when: 'we request for an empty line'
        line = repository.findLine('two')

        then: 'we get an empty metro line'
        line == []

        when: 'we request a non existing line'
        line = repository.findLine('unknown')

        then: 'we got a null'
        line == null
    }


    def "AddHead"() {
    }

    def "Append"() {
    }

    def "Remove"() {
    }

    def "Connect"() {
    }

    def "GetGraph"() {
    }

    def "GetMetroName"() {
    }

    def 'should add a new station at the beginning of metro line'() {
    }

    def 'should append a new station at the end of the line'() {
    }

    def 'should connect the source station to the target station'() {
    }

    def 'should remove the metro station from the metro map'() {
    }

}
