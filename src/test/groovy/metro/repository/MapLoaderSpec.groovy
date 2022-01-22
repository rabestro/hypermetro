package metro.repository

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

class MapLoaderSpec extends Specification {
    def jsonElement = Mock JsonElement
    def jsonObject = Mock JsonObject

    @Subject
    def metroLoader = new MapLoader()

    def "Load"() {
    }

    @Ignore("can't mock final classes")
    def 'should parse StationId'() {
        when:
        metroLoader.parseStationId(jsonObject)

        then:
        2 * jsonObject.get(_) >> jsonElement
        2 * jsonElement.getAsString()
    }
}
