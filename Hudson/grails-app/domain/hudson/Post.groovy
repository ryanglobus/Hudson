package hudson

class Post {

    String link
    String title
    Boolean isNew = true
    Date date

    static belongsTo = [query: Query]

    static constraints = {
        link url: true
        title nullable: true
    }

}
