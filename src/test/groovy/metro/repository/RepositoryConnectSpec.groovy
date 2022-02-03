package metro.repository

import metro.model.Station
import metro.model.StationId
import spock.lang.Specification
import spock.lang.Subject

class RepositoryConnectSpec extends Specification {
    @Subject
    def repository = new MetroRepositoryJson()

    def 'should connect the source station to the target station'() {
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

        and: 'metro map consist of these two metro lines'
        repository.metroMap = [L1: L1, L2: L2]

        expect: 'none of the stations has a transfer to another station'
        L1*.transfer().every { it.isEmpty() }
        L2*.transfer().every { it.isEmpty() }

        when: 'we connect two stations'
        repository.connect('L1', 'A2', 'L2', 'B2')

        then: 'the first station has transfer to the second'
        with(A2.transfer()) {
            size() == 1
            contains new StationId('L2', 'B2')
        }

        and: 'the second station has transfer to the first'
        with(B2.transfer()) {
            size() == 1
            contains new StationId('L1', 'A2')
        }

        and: 'other metro stations do not have transitions to other stations'
        [A1, A3, B1, B3, B4].every { it.transfer().isEmpty() }
    }

}
