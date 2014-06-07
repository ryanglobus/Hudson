package hudson

import grails.test.mixin.TestFor
import spock.lang.Specification
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import hudson.Query.HousingType
import hudson.neighborhood.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Query)
@Mock([Post, Neighborhood, City, Region])
class QuerySpec extends Specification {

    private static String CRAIGSLIST_URL = 'http://sfbay.craigslist.org/'

    private static final String CRAIGSLIST_XML0 =
    """<?xml version="1.0" encoding="iso-8859-1"?>

<rdf:RDF
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns="http://purl.org/rss/1.0/"
 xmlns:enc="http://purl.oclc.org/net/rss_2.0/enc#"
 xmlns:ev="http://purl.org/rss/1.0/modules/event/"
 xmlns:content="http://purl.org/rss/1.0/modules/content/"
 xmlns:dcterms="http://purl.org/dc/terms/"
 xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/"
 xmlns:admin="http://webns.net/mvcb/"
>

<channel rdf:about="http://sfbay.craigslist.org/apa/index.rss">
<title>craigslist | apts/housing for rent in SF bay area</title>
<link>http://sfbay.craigslist.org/apa/</link>
<description></description>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:publisher>robot@craigslist.org</dc:publisher>
<dc:creator>robot@craigslist.org</dc:creator>
<dc:source>http://sfbay.craigslist.org/apa/index.rss</dc:source>
<dc:title>craigslist | apts/housing for rent in SF bay area</dc:title>
<dc:type>Collection</dc:type>
<syn:updateBase>2014-04-18T19:12:21-07:00</syn:updateBase>
<syn:updateFrequency>1</syn:updateFrequency>
<syn:updatePeriod>hourly</syn:updatePeriod>
<items>
 <rdf:Seq>
 </rdf:Seq>
</items>
</channel>
</rdf:RDF>""" // dummy XML string

    private static final String CRAIGSLIST_XML1 =
    """<?xml version="1.0" encoding="iso-8859-1"?>

<rdf:RDF
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns="http://purl.org/rss/1.0/"
 xmlns:enc="http://purl.oclc.org/net/rss_2.0/enc#"
 xmlns:ev="http://purl.org/rss/1.0/modules/event/"
 xmlns:content="http://purl.org/rss/1.0/modules/content/"
 xmlns:dcterms="http://purl.org/dc/terms/"
 xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/"
 xmlns:admin="http://webns.net/mvcb/"
>

<channel rdf:about="http://sfbay.craigslist.org/apa/index.rss">
<title>craigslist | apts/housing for rent in SF bay area</title>
<link>http://sfbay.craigslist.org/apa/</link>
<description></description>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:publisher>robot@craigslist.org</dc:publisher>
<dc:creator>robot@craigslist.org</dc:creator>
<dc:source>http://sfbay.craigslist.org/apa/index.rss</dc:source>
<dc:title>craigslist | apts/housing for rent in SF bay area</dc:title>
<dc:type>Collection</dc:type>
<syn:updateBase>2014-04-18T19:12:21-07:00</syn:updateBase>
<syn:updateFrequency>1</syn:updateFrequency>
<syn:updatePeriod>hourly</syn:updatePeriod>
<items>
 <rdf:Seq>
  <rdf:li rdf:resource="http://sfbay.craigslist.org/sfc/apa/4426589093.html" />
 </rdf:Seq>
</items>
</channel>
<item rdf:about="http://sfbay.craigslist.org/sfc/apa/4426589093.html">
<title><![CDATA[Sat. Open House 10am to 5pm! Must See (please)!! Amazing Location & Great Bldg (lower pac hts) &#x0024;3350 2bd 884sqft]]></title>
<link>http://sfbay.craigslist.org/sfc/apa/4426589093.html</link>
<description><![CDATA[Location: Lower Pacific Heights 
Open House all week Monday to Saturday from 10:00 am to 5:30 pm!! 
Great unfurnished and recently remodeled 2BR/1BA apartment on 4th floor. Modern kitchen with Quartz counters and stainless steel appliances. Tastefull [...]]]></description>
<dc:date>2014-04-18T19:04:50-07:00</dc:date>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:source>http://sfbay.craigslist.org/sfc/apa/4426589093.html</dc:source>
<dc:title><![CDATA[Sat. Open House 10am to 5pm! Must See (please)!! Amazing Location & Great Bldg (lower pac hts) &#x0024;3350 2bd 884sqft]]></dc:title>
<dc:type>text</dc:type>
<enc:enclosure resource="http://images.craigslist.org/00b0b_eNqi8svkKON_300x300.jpg" type="image/jpeg"/>
<dcterms:issued>2014-04-18T19:04:50-07:00</dcterms:issued>
</item>
</rdf:RDF>""" // dummy XML string

    private static final String CRAIGSLIST_XML3 =
        """<?xml version="1.0" encoding="iso-8859-1"?>

<rdf:RDF
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns="http://purl.org/rss/1.0/"
 xmlns:enc="http://purl.oclc.org/net/rss_2.0/enc#"
 xmlns:ev="http://purl.org/rss/1.0/modules/event/"
 xmlns:content="http://purl.org/rss/1.0/modules/content/"
 xmlns:dcterms="http://purl.org/dc/terms/"
 xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/"
 xmlns:admin="http://webns.net/mvcb/"
>

<channel rdf:about="http://sfbay.craigslist.org/apa/index.rss">
<title>craigslist | apts/housing for rent in SF bay area</title>
<link>http://sfbay.craigslist.org/apa/</link>
<description></description>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:publisher>robot@craigslist.org</dc:publisher>
<dc:creator>robot@craigslist.org</dc:creator>
<dc:source>http://sfbay.craigslist.org/apa/index.rss</dc:source>
<dc:title>craigslist | apts/housing for rent in SF bay area</dc:title>
<dc:type>Collection</dc:type>
<syn:updateBase>2014-04-18T19:12:21-07:00</syn:updateBase>
<syn:updateFrequency>1</syn:updateFrequency>
<syn:updatePeriod>hourly</syn:updatePeriod>
<items>
 <rdf:Seq>
  <rdf:li rdf:resource="http://sfbay.craigslist.org/sfc/apa/4426589093.html" />
  <rdf:li rdf:resource="http://sfbay.craigslist.org/sby/apa/4429732232.html" />
  <rdf:li rdf:resource="http://sfbay.craigslist.org/eby/apa/4429624005.html" />
 </rdf:Seq>
</items>
</channel>
<item rdf:about="http://sfbay.craigslist.org/sfc/apa/4426589093.html">
<title><![CDATA[Sat. Open House 10am to 5pm! Must See (please)!! Amazing Location & Great Bldg (lower pac hts) &#x0024;3350 2bd 884sqft]]></title>
<link>http://sfbay.craigslist.org/sfc/apa/4426589093.html</link>
<description><![CDATA[Location: Lower Pacific Heights 
Open House all week Monday to Saturday from 10:00 am to 5:30 pm!! 
Great unfurnished and recently remodeled 2BR/1BA apartment on 4th floor. Modern kitchen with Quartz counters and stainless steel appliances. Tastefull [...]]]></description>
<dc:date>2014-04-18T19:04:50-07:00</dc:date>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:source>http://sfbay.craigslist.org/sfc/apa/4426589093.html</dc:source>
<dc:title><![CDATA[Sat. Open House 10am to 5pm! Must See (please)!! Amazing Location & Great Bldg (lower pac hts) &#x0024;3350 2bd 884sqft]]></dc:title>
<dc:type>text</dc:type>
<enc:enclosure resource="http://images.craigslist.org/00b0b_eNqi8svkKON_300x300.jpg" type="image/jpeg"/>
<dcterms:issued>2014-04-18T19:04:50-07:00</dcterms:issued>
</item>
<item rdf:about="http://sfbay.craigslist.org/sby/apa/4429732232.html">
<title><![CDATA[Recently Reduced price-Renovated 2 bedroom 2 bath! (san jose south) &#x0024;2479 2bd 1038sqft]]></title>
<link>http://sfbay.craigslist.org/sby/apa/4429732232.html</link>
<description><![CDATA[Recently reduced price! This bright 2 bedroom 2 bath Apartment located on the third floor will amaze you, large windows with Almaden Lake Views as you walk into your home and spacious large furniture friendly living room. Not only will you enjoy your [...]]]></description>
<dc:date>2014-04-18T19:04:35-07:00</dc:date>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:source>http://sfbay.craigslist.org/sby/apa/4429732232.html</dc:source>
<dc:title><![CDATA[Recently Reduced price-Renovated 2 bedroom 2 bath! (san jose south) &#x0024;2479 2bd 1038sqft]]></dc:title>
<dc:type>text</dc:type>
<enc:enclosure resource="http://images.craigslist.org/00Z0Z_aHjYO4X35Ph_300x300.jpg" type="image/jpeg"/>
<dcterms:issued>2014-04-18T19:04:35-07:00</dcterms:issued>
</item>
<item rdf:about="http://sfbay.craigslist.org/eby/apa/4429624005.html">
<title><![CDATA[1 Bedroom for (cheap) rent (berkeley) &#x0024;700 1bd]]></title>
<link>http://sfbay.craigslist.org/eby/apa/4429624005.html</link>
<description><![CDATA[your own super large room with great light, the joy of your own bathroom and shower in private No. Berk. home, close to Shattuck shops, cafes, BART, and bus. 
Quiet and safe neighborhood. Close to public transportation (bus and Bart), shopping and hi [...]]]></description>
<dc:date>2014-04-18T19:03:20-07:00</dc:date>
<dc:language>en-us</dc:language>
<dc:rights>&#x26;copy; 2014 &#x3C;span class=&#x22;desktop&#x22;&#x3E;craigslist&#x3C;/span&#x3E;&#x3C;span class=&#x22;mobile&#x22;&#x3E;CL&#x3C;/span&#x3E;</dc:rights>
<dc:source>http://sfbay.craigslist.org/eby/apa/4429624005.html</dc:source>
<dc:title><![CDATA[1 Bedroom for (cheap) rent (berkeley) &#x0024;700 1bd]]></dc:title>
<dc:type>text</dc:type>
<dcterms:issued>2014-04-18T19:03:20-07:00</dcterms:issued>
</item>
</rdf:RDF>""" // dummy XML string

    def setup() {
        
    }

    def cleanup() {
    }

    void "test xmlParsing"() {
        given:
        Region.initializeData()
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance()
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder()
        InputSource input0 = new InputSource(new StringReader(CRAIGSLIST_XML0))
        InputSource input1 = new InputSource(new StringReader(CRAIGSLIST_XML1))
        InputSource input3 = new InputSource(new StringReader(CRAIGSLIST_XML3))
        Document doc0 = dBuilder.parse(input0)
        Document doc1 = dBuilder.parse(input1)
        Document doc3 = dBuilder.parse(input3)
        Query q = new Query(region: Region.sfbay())
        Calendar c1, c2, c3
        c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT-07"))
        c1.clear()
        // the year is 114 + 1900 = 2014
        c1.setTime(new Date(114, Calendar.APRIL, 18, 19, 4, 50))
        c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT-07"))
        c2.clear()
        c2.setTime(new Date(114, Calendar.APRIL, 18, 19, 4, 35))
        c3 = Calendar.getInstance(TimeZone.getTimeZone("GMT-07"))
        c3.clear()
        c3.setTime(new Date(114, Calendar.APRIL, 18, 19, 3, 20))
        Post p1 = new Post(
            link: 'http://sfbay.craigslist.org/sfc/apa/4426589093.html',
            title: "Sat. Open House 10am to 5pm! Must See (please)!! Amazing Location & Great Bldg",
            date: c1)
        Post p2 = new Post(
            link: 'http://sfbay.craigslist.org/sby/apa/4429732232.html',
            title: "Recently Reduced price-Renovated 2 bedroom 2 bath!",
            date: c2)
        Post p3 = new Post(
            link: 'http://sfbay.craigslist.org/eby/apa/4429624005.html',
            title: "1 Bedroom for (cheap) rent",
            date: c3)

        when:
        List<Post> posts0 = q.searchCraigslist(doc0)
        List<Post> posts1 = q.searchCraigslist(doc1)
        List<Post> posts3 = q.searchCraigslist(doc3)

        then:
        posts0 != null
        posts0.size() == 0

        posts1 != null
        posts1.size() == 1
        Post firstPost = posts1.get(0)
        firstPost.link.equals(p1.link)
        firstPost.title.equals(p1.title)
        firstPost.isNew
        firstPost.date.compareTo(p1.date) == 0

        posts3 != null
        posts3.size() == 3
        posts3.every {post ->
            [p1, p2, p3].any {
                post.link.equals(it.link) &&
                post.title.equals(it.title) &&
                post.isNew &&
                post.date.compareTo(it.date) == 0
            }
        }
    }

    void "test craigslistRssUrl"() {
        given:
        Region.initializeData()
        Neighborhood epa = Neighborhood.findByName('east palo alto')
        Neighborhood paloAlto = Neighborhood.findByName('palo alto')
        City peninsula = paloAlto.city
        Region sfbay = peninsula.region
        Query queryMinFields = new Query(region: sfbay)
        Query queryAllFields = new Query(
            region: sfbay,
            city: peninsula,
            searchText: 'unit',
            housingType: HousingType.LOFT.getValue(),
            minRent: 500,
            maxRent: 1000,
            numBedrooms: 2,
            cat: false,
            dog: true)
        queryAllFields.addToNeighborhoods(epa)
        queryAllFields.addToNeighborhoods(paloAlto)
        Query querySomeFields = new Query(
            region: sfbay,
            city: peninsula,
            maxRent: 2000,
            housingType: HousingType.HOUSE.getValue(),
            numBedrooms: 1,
            dog: true)
        Query queryWithEscaping = new Query(region: sfbay, searchText: 'hello world? & hudson')

        when:
        String urlMinFields = queryMinFields.craigslistRssUrl()
        String urlAllFields = queryAllFields.craigslistRssUrl()
        String urlSomeFields = querySomeFields.craigslistRssUrl()
        String urlWithEscaping = queryWithEscaping.craigslistRssUrl()

        then: // TODO allow for more flexibility in parameter ordering
        urlMinFields.equals(CRAIGSLIST_URL +
            "search/apa?catAbb=apa&s=0&format=rss")
        urlAllFields.equals(CRAIGSLIST_URL +
            "search/apa/pen?bedrooms=2&catAbb=apa&housing_type=${HousingType.LOFT.getValue()}&maxAsk=1000&minAsk=500&nh=${epa.value}&nh=${paloAlto.value}&pets_dog=wooof&query=unit&s=0&format=rss")
        urlSomeFields.equals(CRAIGSLIST_URL +
            "search/apa/pen?bedrooms=1&catAbb=apa&housing_type=${HousingType.HOUSE.getValue()}&maxAsk=2000&pets_dog=wooof&s=0&format=rss")
        urlWithEscaping.equals(CRAIGSLIST_URL +
            "search/apa?catAbb=apa&query=hello+world%3F+%26+hudson&s=0&format=rss")
    }

    void "test searchCraigslist"() {
        given:
        Region.initializeData()
        Query q = new Query(region: Region.sfbay())

        when:
        List<Post> posts = q.searchCraigslist()

        then:
        notThrown(Exception)
        posts != null
        posts.size() > 0
    }

    void "test next thing"() {
        given:
        Region.initializeData()

        when:
        User newUser = new User();
        newUser.email = "ckortel@stanford.edu";
        newUser.phone = "4108977488";
        newUser.firstName = "Kelly";
        Post newPost = new Post();
        newPost.link = "asdf link!";
        newPost.isNew = true;
        Query newQuery = new Query(region: Region.sfbay());
        newQuery.user = newUser;

        List<Post> newList = new ArrayList<Post>();
        //newUser.queries.add(newQuery);

        then:
        newUser.notifyUser();
    }
}
