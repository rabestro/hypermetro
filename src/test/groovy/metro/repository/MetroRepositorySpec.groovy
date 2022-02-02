package metro.repository

import metro.model.Station
import metro.model.StationId
import spock.lang.Specification
import spock.lang.Subject

class MetroRepositorySpec extends Specification {
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
    def emptyLine = [] as ArrayDeque<Station>

    @Subject
    def repository = new MetroRepositoryJson()

    void setup() {
        repository.metroMap = [L1: L1, L2: L2, empty: emptyLine]
    }

}
