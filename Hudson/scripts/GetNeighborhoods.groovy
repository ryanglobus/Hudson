/*
 * Usage: groovy GetNeighborhoods.groovy <region> 
 *    Ex: groovy GetNeighborhoods.groovy sfbay
 */
@Grab(group='org.jsoup', module='jsoup', version='1.7.3')
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

public final class Globals {
    static String baseUrl
    static final Integer TIMEOUT = 30_000 // long timeout due to Internet problems
}

if (args.length < 1) {
    println("Usage: groovy GetNeighborhoods.groovy <region>")
    println("   Ex: groovy GetNeighborhoods.groovy sfbay")
    return
}

String region = args[0]
Globals.baseUrl = "http://${region}.craigslist.org"

def getCityNeighborhoods(String city, String cityCode) {
    String url = Globals.baseUrl + "/${cityCode}/apa/"
    println("<!-- Connecting to ${url}... -->")
    Document doc = Jsoup.connect(url).timeout(Globals.TIMEOUT).get()
    Element neighborhoodSelect = doc.select('select[name=nh]').first()
    if (neighborhoodSelect == null) {
        System.err.println('Error: could not find a select tag with name="nh" in the Craigslist HTML')
        System.exit(1)
    }


    println("<city name=\"${city}\" value=\"${cityCode}\">")
    println("<neighborhoods>")
    for (Element option : neighborhoodSelect.select('option')) {
        String val = option.val()
        if (val.isEmpty()) continue
        String text = option.text()
        println("<neighborhood value=\"${val}\">${text}</neighborhood>")
    }
    println("</neighborhoods>")
    println("</city>")
}

String url = Globals.baseUrl + '/apa/'
println("<!-- Connecting to ${url}... -->")
Document doc = Jsoup.connect(url).timeout(Globals.TIMEOUT).get()
Element cityTabs = doc.select('#satabs').first()
if (cityTabs == null) {
    System.err.println('Error: could not find city tabs')
    System.exit(1)
}

println("<region name=\"${region}\">") // TODO doesn't quite match domain
for (Element a : cityTabs.select('a')) {
    String cityCode = a.attr('href').split('/')[1]
    String city = a.text()
    getCityNeighborhoods(city, cityCode)
}
println("</region>")





