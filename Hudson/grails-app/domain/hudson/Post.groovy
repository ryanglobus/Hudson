package hudson

import hudson.Query

class Post {

    String link
    String title
	String replyEmail
    Boolean isNew = Boolean.TRUE //Has a notification been sent?
    Calendar date
	Boolean deleted = Boolean.FALSE     //Has the user marked this post as "seen" on the site?
										//changed to TRUE when the user manually marks it/deletes the post
	Boolean responseSent = Boolean.FALSE 
	Boolean favorite = Boolean.FALSE
	Double longitude;
	Double latitude;
	String photoLink;
	String price;
	String neighborhood;

	
    static belongsTo = [query: Query]

    static constraints = {
        link url: true
        title nullable: true
		replyEmail nullable: true
		photoLink nullable: true
		photoLink uri: true
		price nullable: true
		longitude nullable: true
		latitude nullable: true
        neighborhood nullable: true
    }

}
