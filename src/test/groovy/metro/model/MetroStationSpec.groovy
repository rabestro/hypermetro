package metro.model

import spock.lang.Specification
import spock.lang.Title

@Title("Model of Metro Station")
class MetroStationSpec extends Specification {
    def "should create a metro station with id and time"() {
        given: 'station id'
        def id = new StationId('Bakerloo', 'Waterloo')

        and: 'time to the next station'
        def nextStationTime = 15

        when: 'we create a metro station'
        def station = new MetroStation(id, nextStationTime)

        then: 'all connections are empty'
        with(station) {
            it.next().isEmpty()
            it.prev().isEmpty()
            it.transfer().isEmpty()
        }
    }
}
