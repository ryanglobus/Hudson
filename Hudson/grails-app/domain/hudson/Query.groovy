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
import hudson.queue.Queue
import hudson.queue.StringConverter
import grails.util.Environment
import hudson.neighborhood.*



class Query {

    enum HousingType {
        ANY(0), APARTMENT(1), CONDO(2), COTTAGE_CABIN(3), DUPLEX(4), FLAT(5),
        HOUSE(6), IN_LAW(7), LOFT(8), TOWNHOUSE(9), MANUFACTURED(10),
        ASSISTED_LIVING(11), LAND(12)

        private HousingType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        private final int value;
    }
	
    Integer searchFrequency = 20 // in minutes
    String searchText
	String name
    Integer minRent
    Integer maxRent
    Integer numBedrooms // min number of bedrooms
    Integer housingType = HousingType.ANY.getValue()
    // for Booleans, null means "indifferent"
    Boolean cat
    Boolean dog
    Boolean notify
    Boolean instantReply
    String responseMessage
    // if changing isCancelled from false to true, need to put query back in queue
    Boolean isCancelled = Boolean.FALSE 

	static hasMany = [posts: Post, neighborhoods: Neighborhood]
	static belongsTo = [user: User, region: Region, city: City] // TODO make region default to SFBAY

    static constraints = {
        searchText nullable: true
        minRent nullable: true, min: 0
        maxRent nullable: true, min: 0
        numBedrooms nullable: true, min: 0
        cat nullable: true
        dog nullable: true
        responseMessage nullable: true
        city nullable: true
        neighborhoods nullable: true
    }

    transient static final Queue<Query> queue = new Queue<Query>(queueName(), new StringConverter<Query>() {
        @Override
        String generateString(Query q) {
            return q.id.toString()
        }

        @Override
        Query parseString(String s) {
            try {
                Integer id = s.toInteger()
                return Query.get(id)
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace()
                return null
            }
        }
    })

    private static String queueName() {
        if (Environment.current.equals(Environment.PRODUCTION)) {
            return 'query'
        } else {
            // create a timestamped query for each dev/test run to ensure
            // starting with an empty queue
            return 'query' + Long.toString(System.currentTimeMillis())
        }
    }

    /**
     * Searches for new posts, saves them, and returns them.
     */
    List<Post> searchAndSaveNewPosts() {
        List<Post> posts = searchCraigslist();
        // closure below assumes posts are all unique
        posts = posts.findAll { p ->
            Post.findByLink(p.link) == null
        }
        posts.each { p ->
            p.query = this
            p.save(flush: true, failOnError: true)
        }
        return posts
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
        String domain = "http://${region.value}.craigslist.org/"
        String path = 'search/apa'
        if (city != null) path += "/${city.value}"

        // note that the parameter order is important for tests to pass
        def params = [:]
        if (numBedrooms != null) params['bedrooms'] = numBedrooms
        params['catAbb'] = 'apa'
        if (housingType != HousingType.ANY.getValue()) {
            params['housing_type'] = housingType
        }
        if (maxRent != null) params['maxAsk'] = maxRent
        if (minRent != null) params['minAsk'] = minRent
        if (neighborhoods != null) params['nh'] = neighborhoods.collect { it.value }
        if (cat) params['pets_cat'] = 'purrr'
        if (dog) params['pets_dog'] = 'wooof'
        if (searchText != null) params['query'] = searchText
        params['s'] = '0'
        params['format'] = 'rss'

        URIBuilder uri = new URIBuilder(domain)
        uri.path = path
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
            p.query = null // set when save
			p.replyEmail = ""
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
            // list all fields to be validated
            if (p.validate(['link', 'title', 'date'])) posts.add(p)
        }
        return posts
    }

    /**
     * Given lists of posts from query, filter results and add new ones to
     * database. Assumes posts match this query.
     */
    private void saveNewPosts(List<Post> posts) {
        for (Post nextPost : posts) {
            boolean found = false;
            for (Post nextOldPost : this.posts) { // TODO must be more efficient way
                if (nextOldPost.link == nextPost.link) {
                    found = true;
                }
            }
            if (!found) {
                nextPost.query = this
                nextPost.save(flush: true, failOnError: true)
                addToPosts(nextPost);
            }
        }
    }


}
