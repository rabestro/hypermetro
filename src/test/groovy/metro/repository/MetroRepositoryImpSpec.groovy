package metro.repository

import io.github.joke.spockmockable.Mockable
import metro.model.MetroLine
import metro.model.MetroStation
import metro.model.StationId
import spock.lang.*

@Title("Repository of Metro Map")
@Mockable([MetroLine, MetroStation])
class MetroRepositoryImpSpec extends Specification {
    @Shared
    def lineOne = Mock MetroLine
    @Shared
    def lineTwo = Mock MetroLine

    def stationOne = Mock MetroStation
    def stationTwo = Mock MetroStation

    def metroLoader = Mock MapLoader

    @Subject
    def repository = new MetroRepositoryImpl(metroLoader)

    void setup() {
        repository.metroMap = [
                one: (lineOne),
                two: (lineTwo)
        ]

        lineOne.getStation(_ as String) >> stationOne
        lineTwo.getStation(_ as String) >> stationTwo
    }

    def 'should load a metro map'() {
        given:
        def fileName = 'london.json'

        when:
        repository.load(fileName)

        then:
        1 * metroLoader.load(fileName)
    }

    def "should find metro line by name"() {
        when: 'we request the repository for existing line'
        def metroLine = repository.getLine(lineName)

        then: 'repository returns metro line'
        metroLine.isPresent()

        and: 'the metro line is expected'
        metroLine.get() === expected

        where:
        lineName | expected
        'one'    | lineOne
        'two'    | lineTwo
    }

    @Ignore
    def 'should find a metro station by id'() {
        given:
        def id = new StationId('one', 'first')

        and:
        lineOne.getStation('first') >> Optional.of(stationOne)

        when:
        def station = repository.getStation(id)

        then:
        station.isPresent()

        and:
        station.get() === stationOne

    }
}
