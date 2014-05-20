package home

import HudsonJobs.*
import grails.util.Environment
import hudson.User
import java.security.MessageDigest
import java.security.SecureRandom
import javax.xml.bind.DatatypeConverter
import hudson.Post
import hudson.Query

class HomeController {

    def index() { 
		
	}
	
	
	//The below if else statement seems like it should be taken out..
	def login() {
		boolean validForm = true
		withForm {
			validForm = true
		} .invalidToken {
			flash.message = "Login failed. Please try again."
			redirect(action:'index')
			validForm = false 
		}
		if(!validForm) return
		
		User usr = User.findByEmail(params.username)
		if(usr == null) {
			flash.message = "incorrect username/password"
			redirect(action:'index')
			return
		}
		String hashedPassword = getHashedPassword(params.password, usr.salt)
		if(hashedPassword != usr.passwordHash) {
			flash.message = "incorrect username/password"
			redirect(action:'index')
			return
		}
		session["userid"] = usr.id
		
		//Find out how many new posts they have to update the navbar badge!
		//Better to store it in a session var then have to recalculate the value
		//every time we need to render the navbar.
		def newPostCount = 0
		for(q in usr.queries) {
			def result = Post.findAll {
				query == q && deleted == false
			}
			
			newPostCount += result.size()
		}
		session["newPostCount"] = newPostCount
		
		redirect(controller:"profile")
		
	}

	def logout() {
		session["userid"] = null
		session.invalidate() // invalidate session for security reasons
		redirect(uri: '/')
	}
	
	def register(){}
		
	def newusersession(){		
		boolean validForm
		withForm{
			validForm = true
		} .invalidToken {
		 	 flash.message = "Query failed. Please try again."
		 	 redirect(controller:"profile")
			 validForm = false
		}
		if(!validForm) return
		
		User usr = new User()
		usr.salt = getSalt()		
		usr.passwordHash = getHashedPassword(params.password, usr.salt)
		usr.email = params.email
		usr.firstName = params.firstName
		usr.lastName = params.lastName
		usr.phone = params?.phone
		usr.notifyFrequency = params.frequency.toInteger()
		usr.carrier = User.Carrier.valueOf(params.carrier).getValue()
		usr.save(flush:true, failOnError:true)
		session["userid"] = usr.id
		
		def frequencyInMilliseconds = usr.notifyFrequency * 6000
		//Create the job that notifies the user!
		if (Environment.current.equals(Environment.PRODUCTION))
			frequencyInMilliseconds = usr.notifyFrequency * 60000
		NotifyJob.schedule(frequencyInMilliseconds, -1, [user:usr]) //we want notifications to run forever!
			
		redirect(controller:"profile")
	}
	
	private static String getHashedPassword(String pass, String salt) {
		String password = pass + salt
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256")
		byte[] hash = digest.digest(password.getBytes("UTF-8"))
		return DatatypeConverter.printHexBinary(hash);
	}
	

	private static String getSalt() 
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[32];
		sr.nextBytes(salt);
		return salt.toString();
	}

	def postRegister(){}	
}
