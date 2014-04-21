package hudson

import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.w3c.dom.Node
import org.w3c.dom.Element
import java.text.SimpleDateFormat
import groovyx.net.http.URIBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.FactoryConfigurationError
import javax.xml.parsers.ParserConfigurationException
import java.io.IOException
import org.xml.sax.SAXException

class Query {

    static final String CRAIGSLIST_URL = 'http://sfbay.craigslist.org/'

    Integer searchFrequency = 20 // in minutes
    String searchText
    Integer minRent
    Integer maxRent
    Integer numBedrooms // min number of bedrooms
    String type // TODO what is this?
    // for Booleans, null means "indifferent"
    Boolean cat
    Boolean dog
    Boolean notify
    Boolean instantReply
    String responseMessage

    static hashMany = [posts: Post]
    static belongsTo = [user: User]

    static constraints = {
        searchText nullable: true
        minRent nullable: true, min: 0
        maxRent nullable: true, min: 0
        numBedrooms nullable: true, min: 0
        type nullable: true
        cat nullable: true
        dog nullable: true
        responseMessage nullable: true
    }

    /**
     * Returns null upon failure. Does NOT save list of posts to database.
     */
    List<Post> searchCraigslist() throws FactoryConfigurationError,
            ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance()
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder()
        return searchCraigslist(dBuilder.parse(craigslistRssUrl()))
    }

    String craigslistRssUrl() {
        // note that the parameter order is important for tests to pass
        // TODO add type
        def params = [:]
        if (numBedrooms != null) params['bedrooms'] = numBedrooms
        params['catAbb'] = 'apa'
        if (maxRent != null) params['maxAsk'] = maxRent
        if (minRent != null) params['minAsk'] = minRent
        if (cat) params['pets_cat'] = 'purrr'
        if (dog) params['pets_dog'] = 'wooof'
        if (searchText != null) params['query'] = searchText
        params['s'] = '0'
        params['format'] = 'rss'

        URIBuilder uri = new URIBuilder(CRAIGSLIST_URL)
        uri.path = 'search/apa'
        uri.query = params
        return uri.toString()
    }

    /**
     * xmlDocument: XML Document of posts which much already match the query
     */
    List<Post> searchCraigslist(Document xmlDocument) {
        if (xmlDocument == null)
            throw IllegalArgumentException("xmlDocument in Query.searchCraigslist cannot be null")
        List<Post> posts = new ArrayList<Post>()
        NodeList items = xmlDocument.getElementsByTagName('item')
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i)
            if (!(item instanceof Element)) continue;
            Element elem = (Element) item
            Post p = new Post()
            p.query = this
            p.link = elem.getElementsByTagName('link').item(0)?.getTextContent()
            p.title = elem.getElementsByTagName('title').item(0)?.getTextContent()
            StringBuilder dateSB =
                new StringBuilder(
                elem.getElementsByTagName('dc:date').item(0)?.getTextContent())
            if (dateSB != null) {
                // the last colon, in the time zone, needs to be removed for parsing
                int lastColonIndex = dateSB.lastIndexOf(':')
                if (lastColonIndex == -1) continue;
                dateSB.deleteCharAt(lastColonIndex)
                SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                p.date = Calendar.getInstance()
                p.date.setTime(sdf.parse(dateSB.toString()))
            }
            if (p.validate()) posts.add(p)
        }
        return posts
    }

    /**
     * Given lists of posts from query, filter results and add new ones to
     * database. Assumes posts match this query.
     */
    void saveNewPosts(List<Post> posts) {
        // TODO Kelly
    }
}
