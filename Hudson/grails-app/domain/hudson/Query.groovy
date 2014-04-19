package hudson

class Query {

    Integer searchFrequency // in minutes
    String searchText
    Integer minRent
    Integer maxRent
    Integer numBedrooms // min number of bedrooms
    String type
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

    static mapping = {
        searchFrequency defaultValue: "20"
    }

    /**
     * Returns null upon failure. Does NOT save list of posts to database.
     */
    List<Post> searchCraigslist() {
        // TODO Ryan
        return null
    }

    /**
     * Given lists of posts from query, filter results and add new ones to
     * database. Assumes posts match this query.
     */
    void saveNewPosts(List<Post> posts) {
        // TODO Kelly
    }
}
