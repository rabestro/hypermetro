package metro.repository

import io.github.joke.spockmockable.Mockable
import metro.model.Station
import spock.lang.Specification
import spock.lang.Subject

class MetroRepositoryJsonSpec extends Specification {

    def A1 = new Station('A1', 7, ['A2'] as Set, [] as Set, [] as Set)
    def A2 = new Station('A2', 5, ['A3'] as Set, ['A1'] as Set, [] as Set)
    def A3 = new Station('A3', 9, [] as Set, ['A2'] as Set, [] as Set)

    def L1 = [A1, A2, A3] as ArrayDeque
    def L2 = [] as ArrayDeque

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
