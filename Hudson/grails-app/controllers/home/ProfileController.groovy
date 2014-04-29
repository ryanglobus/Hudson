package home

import hudson.Query
import hudson.User
import HudsonJobs.*


class ProfileController {

	def index() { }

	def newquery() {

		//For some reason the controller is being invoked twice.
		//This if statement makes sure that it does not run the job twice.
		//We may want to make a "New Query Created" page that can allow them links
		//back to the home page, create a query page, etc. At least until/if we can't
		//figure out how to fix this double invocation...everything I've seen doesn't seem to work.
		if (params.size() > 3) {
			Query query = new Query()
			query.searchText = params.searchText
			query.minRent = params.minrent.toInteger()
			query.maxRent = params.maxrent.toInteger()
			query.numBedrooms = params.numRooms.toInteger()
			query.housingType = Query.HousingType.valueOf(params.type).getValue()
			if(params.cat) query.cat = true
			if(params.dog) query.dog = true
			if(params.notify) query.notify = true
			if(params.instantRelpy) {
				query.instantReply = true
				query.responseMessage = params.responseMessage
			}
			query.user = User.findById(session["userid"])
			query.save(flush:true)

			//Create the job to run the query!
			//Job will be run every ten minutes for 30 days.
			CrawlJob.schedule(600000, 4319, [query: query])

			[usr:query.user, query: query]
		}
	}
	
		
}
