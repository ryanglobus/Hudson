package home

import java.nio.file.attribute.UserDefinedFileAttributeView;

import hudson.Query
import hudson.User


class ProfileController {

    def index() { }
	
	def newquery() {
		Query query = new Query()
		query.searchText = params.searchText
		query.minRent = params.minrent.toInteger()
		query.maxRent = params.maxrent.toInteger()
		query.numBedrooms = params.numRooms.toInteger()
		query.type = params.type
		if(params.cat) query.cat = true
		if(params.dog) query.dog = true
		if(params.notify) query.notify = true
		if(params.instantRelpy) {
			query.instantReply = true
			query.responseMessage = params.responseMessage
		}
		query.user = User.findById(session["userid"])
		query.save(flush:true) //See what this returns	
		
		[usr:query.user, query: query]	
	}
	
}
