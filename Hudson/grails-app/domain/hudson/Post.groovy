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
	double longitude;
	double latitude;
	String photoLink;
	String price;
	String neighborhood;
	
    static belongsTo = [query: Query]

    static constraints = {
        link url: true
        title nullable: true
		replyEmail nullable: true
    }

}
