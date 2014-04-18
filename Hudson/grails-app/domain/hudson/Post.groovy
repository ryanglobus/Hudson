package hudson

class Post {

    String link
    String title
    Boolean isNew
    Date date

    static belongsTo = [query: Query]

    static constraints = {
        link url: true
        title nullable: true
    }

    static mapping = {
        isNew defaultValue: "true"
    }
}
