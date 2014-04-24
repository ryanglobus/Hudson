package hudson

class Query {

    enum HousingType {
        APARTMENT(1), CONDO(2), COTTAGE_CABIN(3), DUPLEX(4), FLAT(5),
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
    Integer minRent
    Integer maxRent
    Integer numBedrooms // min number of bedrooms
    Integer housingType
    // for Booleans, null means "indifferent"
    Boolean cat
    Boolean dog
    Boolean notify
    Boolean instantReply
    String responseMessage
    Boolean isCancelled = false

    static hashMany = [posts: Post]
    static belongsTo = [user: User]

    static constraints = {
        searchText nullable: true
        minRent nullable: true, min: 0
        maxRent nullable: true, min: 0
        numBedrooms nullable: true, min: 0
        housingType nullable: true
        cat nullable: true
        dog nullable: true
        responseMessage nullable: true
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
