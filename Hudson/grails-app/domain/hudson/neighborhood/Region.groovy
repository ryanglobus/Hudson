package hudson.neighborhood
import java.io.FileInputStream
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class Region {

    String name
    String value // URL is http://${value}.craigslist.org

    static hasMany = [cities: City]

    static constraints = {
        name blank: false, unique: true
        value blank: false, unique: true
    }

    static mapping = {
        cities sort: 'name'
    }

    /**
     * This method is here because for some reason while testing, 
     * Bootstrap.groovy does not run. Thus, tests can call this to initialize
     * Region/City/Neighborhood data.
     */
    static void initializeData() {
        Region sfbay = Region.findOrCreateWhere(name: 'SF Bay Area',
            value: 'sfbay')
        sfbay.save(flush: true, failOnError: true)

        // load neighborhood data from the XML
        FileInputStream neighborhoodFile = null
        try {
            neighborhoodFile = new FileInputStream('neighborhoods.xml')
            Document doc = Jsoup.parse(neighborhoodFile, null, '.', Parser.xmlParser())
            for (Element regionElem : doc.select('region')) {
                String regionName = regionElem.attr('name')
                String regionValue = regionElem.attr('value')
                Region region = Region.findOrCreateWhere(name: regionName,
                    value: regionValue)
                region.save(flush: true, failOnError: true)
                for (Element cityElem : regionElem.select('city')) {
                    City city = City.findOrCreateWhere(
                        name: cityElem.attr('name'),
                        value: cityElem.attr('value'),
                        region: region)
                    city.save(flush: true, failOnError: true)
                    for (Element nhElem : cityElem.select('neighborhoods neighborhood')) {
                        Neighborhood nh = Neighborhood.findOrCreateWhere(
                            name: nhElem.text(),
                            value: nhElem.attr('value').toInteger(),
                            city: city)
                        nh.save(flush: true, failOnError: true)
                    }
                }
            }
        } finally {
            if (neighborhoodFile != null) neighborhoodFile.close()
        }
    }

    static Region sfbay() {
        return Region.findByValue('sfbay')
    }

}
