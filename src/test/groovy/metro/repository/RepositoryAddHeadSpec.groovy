package metro.repository

import metro.model.Station
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

@Title("Specifications for 'add-head' command")
class RepositoryAddHeadSpec extends Specification{
    @Subject
    def repository = new MetroRepositoryJson()

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

}
