package metro.repository

import metro.model.Station
import spock.lang.Title

@Title("Repository 'append' command specifications")
class AppendSpec extends MetroRepositorySpec {

    def 'should thrown an exception for non existing metro line'() {
        given: "the metro map returns null for non-existing line"
        repository.metroMap = Stub (Map) {
            it.get(_) >> null
        }

        when: 'we are trying to append a station to non-existing metro line'
        repository.append(metroLine, 'some station', 5)

        then: 'null pointer exception thrown'
        def exception = thrown(NullPointerException)

        and: 'the error message explains the reason'
        exception.message.contains "no metro line with the name $metroLine"

        where: "the line doesn't exist on the metro map"
        metroLine = 'some non existing line'
    }

    def 'should thrown an exception if the station is already exist'() {
        given: "the metro line already has a station"
        repository.metroMap = [(metroLine): [new Station(stationName, time)] as ArrayDeque]

        when: 'we try to append this station'
        repository.append(metroLine, stationName, time)

        then: 'illegal argument exception thrown'
        def exception = thrown(IllegalArgumentException)

        and: 'the error message explains the reason'
        exception.message == "the station with the name '$stationName' is already on the metro line"

        where: "the station name and metro line name as"
        metroLine = 'Bakerloo'
        stationName = 'Waterloo'
        time = 5
    }

    def 'should append a new station to the empty metro line'() {
        when: 'we append a stationName to the empty metro line'
        repository.append('empty', stationName, timeToNextStation)

        then: 'the line become not empty'
        emptyLine

        and: 'it has exactly one stationName'
        emptyLine.size() == 1

        and: 'the new created and added stationName has expected properties'
        with(emptyLine.first) {
            name() == stationName
            time() == timeToNextStation
            next().isEmpty()
            prev().isEmpty()
            transfer().isEmpty()
        }

        where: 'station name and time to the next station defined as'
        stationName           | timeToNextStation
        'Harrow & Wealdstone' | 5
        'Warwick Avenue'      | 7
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

        when: 'we append a stationName at the end of the first metro line'
        repository.append('one', 'S4', 4)

        then: 'the metro line has stations in correct order'
        lineOne*.name() == ['S1', 'S2', 'S3', 'S4']

        and: 'the new added stationName has correctly defined fields'
        with(lineOne.last) {
            name() == 'S4'
            next() == ['S1'] as Set
            prev() == ['S3'] as Set
            transfer().isEmpty()
            time() == 4
        }

        and: 'the previous last stationName was correctly modified'
        with(S3) {
            name() == 'S3'
            next() == ['S4'] as Set
            prev() == ['S2'] as Set
            transfer().isEmpty()
            time() == 9
        }

        when: 'we append a new stationName to the second empty metro line'
        repository.append('two', 'X', 5)

        then: 'the second line become not empty'
        lineTwo

        and: 'it has exactly one stationName'
        lineTwo.size() == 1

        and: 'the new stationName has expected fields'
        with(lineTwo.first) {
            name() == 'X'
            time() == 5
            next().isEmpty()
            prev().isEmpty()
            transfer().isEmpty()
        }

    }


}
