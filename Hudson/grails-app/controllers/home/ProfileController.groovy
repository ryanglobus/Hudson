package home

import java.util.regex.Pattern.Ques;

import hudson.Query
import grails.util.Environment
import hudson.User
import HudsonJobs.*
import hudson.Post
import hudson.neighborhood.*
import org.codehaus.groovy.grails.web.json.*
import groovy.hudson.queue.Message
import groovy.hudson.queue.QueueException
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
		
		//User cannot make two queries with the same name.
		//Causes issues elsewhere and is just generally confusing.
		def me = User.get(session["userid"])
		def alreadyExists = Query.findAll {
			user == me && name == params.queryName
		}
		if(alreadyExists.size() != 0) {
			flash.message = "You already have a query called " + params.queryName + "! Please choose a different name for your new query!"
			redirect(action:'index')
			return
		}
		
		Query query = setUpQuery(params, true)

		// Enqueue the job to run the query
		Message<Query> msg = new Message<Query>(query)
		msg.delay = 10 // short wait at first
		try {
			Query.queue.enqueue(msg)
		} catch (QueueException qe) {
			query.delete()
			throw qe;
		}
		String flash = null
		try {
			query.searchAndSaveNewPosts() // get results now
		} catch (Exception e) {
			e.printStackTrace()
			flash = "Your query was saved but there was a problem loading posts. Try refreshing this page in a minute."
		}

		redirect(action: "newResults", params: [queryName: query.name, favorites: false, flash: flash, sortParam:"date", needsPhoto:false])
	}

	def queryCreated() {
		def usr = User.get(session["userid"])
		def query = Query.get(params.queryid)
		[query: query, usr: usr, housingType: params.housingType]
	}

	def newResults() {
		def usr = User.get(session["userid"])
		def results = [:]
		def lats = []
		def lons = []
		def links = []
		def titles = []
		def queriesUsed = []
		def queries = usr.queries
		def queryTitle = ""
		def favorites = params.favorites.toBoolean()
		if (params.flash != null) flash.message = params.flash
		def needsPhoto = params.needsPhoto.toBoolean()
		def sortOrder = "desc"
		def sortValue = params.sortParam;
		
		if(params.sortParam == "priceAsc" || params.sortParam == "priceDesc")
			sortValue = "price"
		
		if(params.sortParam == "priceAsc")
			sortOrder = "asc"

		if(params.queryName == "all") {
			queryTitle = "All Queries"
		}
		else {
			queryTitle = params.queryName
		}

		for (q in queries) {
			if (q.isCancelled == false) {
				queriesUsed.add(q)
				def tempRes = []
				
				if(favorites == false) {
					if(needsPhoto)	{
						tempRes = Post.findAll(sort:sortValue, order:sortOrder) {
							query == q && deleted == false && photoLink != null
						}
					} else {
						tempRes = Post.findAll(sort:sortValue, order:sortOrder) {
							query == q && deleted == false
						}
					}
				}
				else {
					if(needsPhoto) {
						tempRes = Post.findAll(sort:sortValue, order:sortOrder) {
							query == q && deleted == false && favorite == true && photoLink != null
						}
					} else {
						tempRes = Post.findAll(sort:sortValue, order:sortOrder) {
							query == q && deleted == false && favorite == true
						}
					}
				}

				if (params.queryName != "all") {
					if(q.name == queryTitle)
						results.put(q.name, tempRes)
				}
				else {
					if(tempRes.size() != 0)
						results.put(q.name, tempRes)
				}
			
			}
		}
		
		for(postList in results) {
			for(p in postList.value) {
				lats.add(p.latitude)
				lons.add(p.longitude)
				links.add(p.link)
				titles.add(p.title)
			}
		}

		def shmoobli = links as grails.converters.JSON
		def boobli = titles as grails.converters.JSON

		[results: results, queryTitle:queryTitle, queries: queriesUsed, favorites: favorites, sortParam:params.sortParam, needsPhoto: needsPhoto, lats : lats, lons : lons, links : shmoobli, titles : boobli]
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


		redirect(action: "newResults", params:[queryName: params.queryName, favorites:params.favorites.toBoolean(), sortParam: params.sortParam, needsPhoto:params.needsPhoto.toBoolean()])
	}
	
	//'Deletes' and individual query so that it is no longer viewable by the user.
	def deleteQuery() {
		def query = Query.findByName(params.queryName)
		query.isCancelled = true
		query.save(flush:true, failOnError: true)
		
		//IF you delete a query you must also delete all of it's posts!
		//Here we don't actually delete the DB entry, since the things will stick
		//around in the "archive" section for up to a month.
		for(post in query.posts) {
			post.deleted = true
			post.save(flush:true, failOnError: true)
		}
		
		redirect(action: "newResults", params:[queryName: "all", favorites:params.favorites.toBoolean(), sortParam: params.sortParam, needsPhoto:params.needsPhoto.toBoolean()])
	}
	
	//Allows the user to edit a query!
	def editQuery() {
		boolean validForm
		withForm {
			validForm = true
		} .invalidToken {
			flash.message = "Form token test failed"
			redirect(action:'settings')
			validForm = false
		}
		if(!validForm) return
		
		//User cannot make two queries with the same name.
		//This is of course allowed if they are keeping the name the same
		//for the query they are editing : )
		def me = User.get(session["userid"])
		def alreadyExists = Query.findAll {
			user == me && name == params.queryName && id != params.qryId
		}
		if(alreadyExists.size() != 0) {
			flash.message = "You already have a query called " + params.queryName + "! Please choose a different name for your new query!"
			redirect(action:'settings')
			return
		}
		
		//For now let's just make the query and return to settings.
		setUpQuery(params, false)
		redirect(action: "settings")
	}
	
	//Settings allows you to change your password as well as edit queries!!
	def settings() {
		def usr = User.get(session["userid"])
		
		[queries: usr.queries]
	}
	
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
	
	def markAsResponded() {
		Post post = Post.get(params.postId)
		
		if (post.responseSent == true) {
			post.responseSent = false
		}
		else {
			post.responseSent = true
		}
		post.save(flush:true, failOnError:true)
		
		render post as JSON
	}
	
	def markAsFavorite() {
		Post post = Post.get(params.postId)
		
		if (post.favorite == true) {
			post.favorite = false
		}
		else {
			post.favorite = true
		}
		post.save(flush:true, failOnError:true)
		render post as JSON
	}
	
	//Code for making/editing a query, used in newquery as well as in editQuery
	private Query setUpQuery(params, boolean isNew) {
		Query query = null
		if (isNew) {
			query = new Query()
		}
		else{
			query = Query.get(params.qryId)
		}
		
		query.name = params.queryName
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
		
		return query
	}
	
	private static boolean passwordCheck(String pass, User usr) {
		String hashedPassword = HomeController.getHashedPassword(pass, usr.salt)
		if(hashedPassword != usr.passwordHash) return false
		return true
	}
	
}
