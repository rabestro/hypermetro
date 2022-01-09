package metro.model

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Model of Metro Line")
class MetroLineSpec extends Specification {

    static final BAKERLOO = 'Bakerloo'
    static final WATERLOO = 'Waterloo'

    static final WATERLOO_ID = new StationId(BAKERLOO, 'Waterloo')
    static final ELEPHANT_ID = new StationId(BAKERLOO, 'Lambeth North')
    static final LAMBETH_ID = new StationId(BAKERLOO, 'Elephant & Castle')

    @Subject
    def metroLine = new MetroLine(BAKERLOO)

    MetroStation waterloo
    MetroStation elephant
    MetroStation lambeth

    void setup() {
        waterloo = new MetroStation(new StationId(BAKERLOO, 'Waterloo'))
        lambeth = new MetroStation(new StationId(BAKERLOO, 'Lambeth North'))
        elephant = new MetroStation(new StationId(BAKERLOO, 'Elephant & Castle'))
    }

    void cleanup() {
    }

    def "GetStation"() {
    }

    def "Remove"() {
    }

    def "AddHead"() {
    }

    def "Add"() {
    }

    def "should append a station to the empty line"() {
        given: 'the metro line created with no stations'
        def line = new MetroLine(BAKERLOO)

        when: 'we append a station to the empty line'
        line.append(WATERLOO)

        and: "request the metro station by it's name"
        def actual = line.getStation(WATERLOO)

        then: 'there is only one station on the line'
        line.stations().size() == 1

        and: 'we got the metro station'
        actual.isPresent()

        and: 'the actual station equals to the expected'
        actual.get() == expected

        where: 'expected station has correct station id'
        expected = new MetroStation(new StationId(BAKERLOO, WATERLOO))
    }

    def "should get the name of the metro line"() {
        given: 'the metro line created'
        def line = new MetroLine(name)

        expect: 'we get the correct metro line name'
        line.name() == name

        where:
        name << ['Bakerloo', 'Circle', 'Central', 'Hammersmith & City']
    }

    def "GetStations"() {
    }
}
