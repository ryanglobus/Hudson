package home

import hudson.Query
import hudson.User
import HudsonJobs.*


class ProfileController {

	def index() { }

	def newquery() {
		boolean validForm
		withForm {
			validForm = true
		} .invalidToken {
			flash.message = "Form token test failed"
			redirect(action:'index')
			validForm = false
		}
		if(!validForm) return

		if (params.size() > 3) {
			Query query = new Query()
			query.searchText = params?.searchText
			if(params.minrent.length() == 0) query.minRent = null
			else query.minRent = params.minrent.toInteger()
			if(params.maxrent.length() == 0) query.maxRent = null
			else query.maxRent = params.maxrent.toInteger()
			if(params.numRooms.length() == 0) query.numBedrooms = null
			else query.numBedrooms = params.numRooms.toInteger()
			query.housingType = Query.HousingType.valueOf(params.type).getValue()
			if(params.cat) query.cat = true
			if(params.dog) query.dog = true
			query.notify = params.notify
			if(params.instantRelpy) {
				query.instantReply = true
				query.responseMessage = params.responseMessage
			}
			query.user = User.findById(session["userid"])
			query.save(flush:true, failOnError: true)

			CrawlJob.schedule(600000, 4319, [query: query])
			[usr:query.user, query: query]
		}
	}
}
