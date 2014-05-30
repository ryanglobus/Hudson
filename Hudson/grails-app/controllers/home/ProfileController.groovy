package home

import hudson.Query
import grails.util.Environment
import hudson.User
import HudsonJobs.*
import hudson.Post
import hudson.neighborhood.*
import groovy.hudson.queue.Message
import grails.converters.*


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


		Query query = new Query()
		// TODO gracefully handle errors with params
		query.name = params.queryName // TODO assert unique
		query.searchText = params.searchText
		if(params.minrent.length() == 0) query.minRent = null
		else query.minRent = params.minrent.toInteger()
		if(params.maxrent.length() == 0) query.maxRent = null
		else query.maxRent = params.maxrent.toInteger()
		if(params.numRooms.length() == 0) query.numBedrooms = null
		else query.numBedrooms = params.numRooms.toInteger()
		query.housingType = Query.HousingType.valueOf(params.type).getValue()

		if (params.region != null) {
			query.region = Region.findByValue(params.region)
		}
		if (params.city != null) {
			query.city = City.findByRegionAndValue(query.region, params.city)
		}
		if (query.city != null && params.neighborhoods != null) {
			params.list('neighborhoods').each { nh ->
				Neighborhood neighborhood = Neighborhood.findByCityAndValue(query.city, nh.toInteger())
				if (neighborhood != null) {
					query.addToNeighborhoods(neighborhood)
				}
			}
		}

		if(params.cat) query.cat = true
		else query.cat = false

		if(params.dog) query.dog = true
		else query.dog = false

		if(params.notify) query.notify = true
		else query.notify = false

		if(params.instantReply) {
			query.instantReply = true
			query.responseMessage = params.responseMessage
		}
		else query.instantReply = false

		query.user = User.findById(session["userid"])
		query.save(flush:true, failOnError: true)

		// TODO below is slow, and what if it fails?

		// run the query now to get results
		query.searchAndSaveNewPosts()

		// Enqueue the job to run the query
		Message<Query> msg = new Message<Query>(query)
		if (Environment.current.equals(Environment.PRODUCTION)) {
			msg.delay = 600 // every 10 minutes
		} else {
			msg.delay = 60 // every minute
		}
		Query.queue.enqueue(msg)

		redirect(action:"queryCreated", params: [queryid : query.id, housingType: params.type])
	}

	def queryCreated() {
		def usr = User.get(session["userid"])
		def query = Query.get(params.queryid)
		[query: query, usr: usr, housingType: params.housingType]
	}

	def newResults() {
		def usr = User.get(session["userid"])
		def results = [:]
		def queryNames = []
		def queries = usr.queries
		def queryTitle = ""
		def qry = null

		if(params.queryName == "all") {
			queryTitle = "All Queries"
		}
		else {
			qry = Query.findByName(params.queryName)
			queryTitle = params.queryName
		}

		for (q in queries) {
			if (q.isCancelled == false) {
				queryNames.add(q.name)
				def tempRes = Post.findAll {
					query == q && deleted == false
				}

				if (params.queryName != "all") { // TODO can I name my query all?
					if(q == qry)
						results.put(q.name, tempRes)
				}
				else {
					if(tempRes.size() != 0)
						results.put(q.name, tempRes)
				}
			}
		}

		[results: results, queryTitle:queryTitle, queryNames: queryNames]
	}

	//This action is called when the user chooses to delete posts from the "new post" page
	//It deletes the post and then redirects back to the new, updated, new post page.
	//In the future we may want to make these deletions possible using AJAX/javascript.

	//NOTE: Deleted posts are still saved, they just don't show up on the
	//"newPosts" page (think like g-mail!)
	def deletePosts() {
		def postIds = params.list('delete')

		for (singleId in postIds) {
			def post = Post.get(singleId)
			post.deleted = true
			post.save(flush: true, failOnError: true)
		}


		redirect(action: "newResults", params:[queryName: params.queryName])
	}
	
	//'Deletes' and individual query so that it is no longer viewable by the user.
	def deleteQuery() {
		def query = Query.findByName(params.queryName)
		query.isCancelled = true
		query.save(flush:true, failOnError: true)
		
		redirect(action: "newResults", params:[queryName: "all"])
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

	def getNeighborhoods() { // TODO add region param too
		City city = null
		int statusCode = 200
		if (params.city == null) {
			statusCode = 400
		}
		else {
			city = City.findByValue(params.city)
			if (city == null) statusCode = 404
		}
		if (city == null) {
			render(text: "{\"error\": \"Cannot find neighborhoods for the requested city\"}",
				contentType: "application/json", status: statusCode)
			return
		}
		render city.neighborhoods as JSON
	}
	
	private static boolean passwordCheck(String pass, User usr) {
		String hashedPassword = HomeController.getHashedPassword(pass, usr.salt)
		if(hashedPassword != usr.passwordHash) return false
		return true
	}
}
