package hudson.neighborhood

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Region)
@Mock([City, Neighborhood])
class RegionSpec extends Specification {

    def setup() {
        Region.initializeData()
    }

    def cleanup() {
    }

    void "test region"() {
        given:

        when:
        Region sfbay = Region.findByValue('sfbay')
        Neighborhood castro = Neighborhood.findByName('castro / upper market')
        Neighborhood aptos = Neighborhood.findByName('aptos')

        then:
        Region.count() > 0
        sfbay != null
        sfbay.cities.size() > 0
        castro != null
        castro.city.value == 'sfc'
        castro.city.neighborhoods.size() > 0
        castro.city.region.id == sfbay.id

        aptos != null
        aptos.city.name == 'santa cruz'
        aptos.city.region.id == sfbay.id
    }
}


