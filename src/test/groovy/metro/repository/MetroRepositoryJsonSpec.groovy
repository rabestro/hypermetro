package metro.repository

import metro.model.Station
import metro.model.StationId
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Subject

class MetroRepositoryJsonSpec extends Specification {

    def L1A2 = new StationId('L1', 'A2')
    def L2B2 = new StationId('L2', 'B2')

    // Subway stations creating a circular subway line.
    def A1 = new Station('A1', 7, ['A2'] as Set, ['A3'] as Set, [] as Set)
    def A2 = new Station('A2', 5, ['A3'] as Set, ['A1'] as Set, [L2B2] as Set)
    def A3 = new Station('A3', 9, ['A1'] as Set, ['A2'] as Set, [] as Set)

    // Subway stations that create an open subway line.
    def B1 = new Station('B1', 6, ['B2'] as Set, [] as Set, [] as Set)
    def B2 = new Station('B2', 8, ['B3'] as Set, ['B1'] as Set, [L1A2] as Set)
    def B3 = new Station('B3', 7, ['B4'] as Set, ['B2'] as Set, [] as Set)
    def B4 = new Station('B3', 9, [] as Set, ['B3'] as Set, [] as Set)

    def L1 = [A1, A2, A3] as ArrayDeque
    def L2 = [B1, B2, B3, B4] as ArrayDeque

    @Subject
    def repository = new MetroRepositoryJson()

    void setup() {
    }

    void cleanup() {
    }

    def "should find metro line by name"() {
        given: 'the repository with given metro map'
        repository.metroMap = [one: L1, two: L2]

        when: 'we request the repository for existing line'
        def line = repository.findLine('one')

        then: 'it returns expected metro line'
        line === L1

        when: 'we request for an empty line'
        line = repository.findLine('two')

        then: 'we get an empty metro line'
        line === L2

        when: 'we request a non existing line'
        line = repository.findLine('unknown')

        then: 'we got a null'
        line == null
    }

    def 'should add-head a new station to the empty metro line'() {
        given: 'an empty metro line'
        def metroLine = [] as ArrayDeque<Station>

        and: 'subway map containing this metro line'
        repository.metroMap = [one: metroLine]

        when: 'we add-head a station to this metro line'
        repository.addHead('one', station, nextTime)

        then: 'the line become not empty'
        metroLine

        and: 'it has exactly one station'
        metroLine.size() == 1

        and: 'the new created and added station has expected properties'
        with(metroLine.first) {
            name() == station
            time() == nextTime
            next().isEmpty()
            prev().isEmpty()
            transfer().isEmpty()
        }

        where: 'station and travel time to the next station defined as'
        station               | nextTime
        'Harrow & Wealdstone' | 5
        'Warwick Avenue'      | 7
    }

    def "should add a new station to the head of metro line"() {
        given: 'three linked in cycle metro stations'
        def S1 = new Station('S1', 7, ['S2'] as Set, ['S3'] as Set, [] as Set)
        def S2 = new Station('S2', 5, ['S3'] as Set, ['S1'] as Set, [] as Set)
        def S3 = new Station('S3', 9, ['S1'] as Set, ['S2'] as Set, [] as Set)

        and: 'the first metro line containing these three stations'
        def lineOne = [S1, S2, S3] as ArrayDeque

        and: 'the second metro line without any stations'
        def lineTwo = [] as ArrayDeque<Station>

        and: 'subway map containing these two metro lines'
        repository.metroMap = [one: lineOne, two: lineTwo]

        when: 'we append a station at the end of the first metro line'
        repository.addHead('one', 'S4', 4)

        then: 'the metro line has stations in correct order'
        lineOne*.name() == ['S4', 'S1', 'S2', 'S3']
        lineOne*.time() == [4, 7, 5, 9]

        and: 'the first new station has correctly defined fields'
        with(lineOne.first) {
            name() == 'S4'
            next() == ['S1'] as Set
            prev() == ['S3'] as Set
            transfer().isEmpty()
            time() == 4
        }

        and: 'the previous first station was correctly modified'
        with(S1) {
            name() == 'S1'
            next() == ['S2'] as Set
            prev() == ['S4'] as Set
            transfer().isEmpty()
            time() == 7
        }

    }

    def "should append a new station to the end of metro line"() {
        given: 'three linked in cycle metro stations'
        def S1 = new Station('S1', 7, ['S2'] as Set, ['S3'] as Set, [] as Set)
        def S2 = new Station('S2', 5, ['S3'] as Set, ['S1'] as Set, [] as Set)
        def S3 = new Station('S3', 9, ['S1'] as Set, ['S2'] as Set, [] as Set)

        and: 'the first metro line containing these three stations'
        def lineOne = [S1, S2, S3] as ArrayDeque

        and: 'the second metro line without any stations'
        def lineTwo = [] as ArrayDeque<Station>

        and: 'subway map containing these two metro lines'
        repository.metroMap = [one: lineOne, two: lineTwo]

        when: 'we append a station at the end of the first metro line'
        repository.append('one', 'S4', 4)

        then: 'the metro line has stations in correct order'
        lineOne*.name() == ['S1', 'S2', 'S3', 'S4']

        and: 'the new added station has correctly defined fields'
        with(lineOne.last) {
            name() == 'S4'
            next() == ['S1'] as Set
            prev() == ['S3'] as Set
            transfer().isEmpty()
            time() == 4
        }

        and: 'the previous last station was correctly modified'
        with(S3) {
            name() == 'S3'
            next() == ['S4'] as Set
            prev() == ['S2'] as Set
            transfer().isEmpty()
            time() == 9
        }

        when: 'we append a new station to the second empty metro line'
        repository.append('two', 'X', 5)

        then: 'the second line become not empty'
        lineTwo

        and: 'it has exactly one station'
        lineTwo.size() == 1

        and: 'the new station has expected fields'
        with(lineTwo.first) {
            name() == 'X'
            time() == 5
            next().isEmpty()
            prev().isEmpty()
            transfer().isEmpty()
        }

    }

    @FailsWith(UnsupportedOperationException)
    def 'should throw an exception for unimplemented remove operation'() {
        given: 'we try to remove any station from any metro line'
        repository.remove(_ as String, _ as String)
    }

    def "should connect two station from different metro lines"() {
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
