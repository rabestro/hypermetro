package metro.repository

import metro.model.Station
import metro.model.StationId
import spock.lang.*

import java.nio.file.Path

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
        given: 'subway stations creating a circular subway line'
        def A1 = new Station('A1', 7, ['A2'] as Set, ['A3'] as Set, [] as Set)
        def A2 = new Station('A2', 5, ['A3'] as Set, ['A1'] as Set, [] as Set)
        def A3 = new Station('A3', 9, ['A1'] as Set, ['A2'] as Set, [] as Set)
        def L1 = [A1, A2, A3] as ArrayDeque

        and: 'subway stations that create an open subway line'
        def B1 = new Station('B1', 6, ['B2'] as Set, [] as Set, [] as Set)
        def B2 = new Station('B2', 8, ['B3'] as Set, ['B1'] as Set, [] as Set)
        def B3 = new Station('B3', 7, ['B4'] as Set, ['B2'] as Set, [] as Set)
        def B4 = new Station('B3', 9, [] as Set, ['B3'] as Set, [] as Set)
        def L2 = [B1, B2, B3, B4] as ArrayDeque

        and: 'subway map containing these two unconnected metro lines'
        repository.metroMap = [one: L1, two: L2]

        expect: 'that every stations on line one and two has no transfers'
        L1*.transfer().every { it.isEmpty() }
        L2*.transfer().every { it.isEmpty() }

        when: 'we connect station A2 with station B3'
        repository.connect('one', 'A2', 'two', 'B3')

        then: 'station A2 from line one has transfer to line two, station B3'
        A1.transfer().isEmpty()
        A3.transfer().isEmpty()
        with(A2) {
            transfer().size() == 1
            transfer().contains new StationId('two', 'B3')
        }

        and: 'station B3 from line two has transfer to line one, station A2'
        B1.transfer().isEmpty()
        B2.transfer().isEmpty()
        B4.transfer().isEmpty()
        with(B3) {
            transfer().size() == 1
            transfer().contains new StationId('one', 'A2')
        }
    }

    @IgnoreIf({ os.windows })
    def "should return metro name on UNIX machines"() {
        given:
        repository.schemaPath = Path.of(fileName)

        expect:
        repository.getMetroName() == metroName

        where:
        fileName                | metroName
        'london.json'           | 'london'
        'london.jsn'            | 'london'
        './london.json'         | 'london'
        '../london.json'        | 'london'
        '/usr/home/london.json' | 'london'
    }

    @Requires({ os.windows })
    def "should return metro name on Windows machines"() {
        given:
        repository.schemaPath = Path.of(fileName)

        expect:
        repository.getMetroName() == metroName

        where:
        fileName                  | metroName
        'london.json'             | 'london'
        'london.jsn'              | 'london'
        /.\london.json/           | 'london'
        /..\london.json/          | 'london'
        /c:\usr\home\london.json/ | 'london'
    }

    def 'should add a new station at the beginning of metro line'() {
    }

    def 'should append a new station at the end of the line'() {
    }

    def 'should remove the metro station from the metro map'() {
    }

}
