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
			if(params.instantReply) {
				query.instantReply = true
				query.responseMessage = params.responseMessage
			} else {
				query.instantReply = false
			}
			query.user = User.findById(session["userid"])
			query.save(flush:true, failOnError: true)

			CrawlJob.schedule(600000, 4319, [query: query])
			[usr:query.user, query: query]
		}
	}
	
	def settings() {}
	
	def changePassword() {
		boolean validForm
		withForm {
			validForm = true
		} .invalidToken {
			flash.message = "Form token test failed"
			redirect(action:'index')
			validForm = false
		}
		if(!validForm) return
		
		User usr = User.findById(session["userid"])	
		if(!passwordCheck(params.oldPassword, usr)) {
			flash.message = "Error: Please enter your old password"
			redirect(action:'settings')
			return
		}
		
		if(params.newPassword != params.confirmPassword) { //or .equals?
			flash.message = "Error: Make sure you confirm the correct password"
			redirect(action:'settings')
			return
		}
		
		usr.salt = HomeController.getSalt()
		usr.passwordHash = HomeController.getHashedPassword(params.newPassword, usr.salt)
		usr.save(flush:true, failOnError:true)
		flash.message = "Your password has been updated."
		redirect(action:'settings')
	}
	
	private static boolean passwordCheck(String pass, User usr) {
		String hashedPassword = HomeController.getHashedPassword(pass, usr.salt)
		if(hashedPassword != usr.passwordHash) return false
		return true
	}
}
