package hudson


class Post {

    String link
    String title
	String replyEmail
    Boolean isNew = true
    Calendar date

    static belongsTo = [query: Query]

    static constraints = {
        link url: true
        title nullable: true
		replyEmail nullable: true
    }

}
