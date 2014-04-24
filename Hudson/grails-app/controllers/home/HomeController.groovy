package home

import hudson.User
import java.security.SecureRandom


class HomeController {

    def index() { 
		
	}
	
	def login() {
		
		if(params.username == "garren" && params.password == "derp") {
			redirect(controller: "profile")	
		} else {
			flash.message = "login failed"
			redirect(action: 'index')
		}
		
	}
	
	//User information and their first search query.
	//JUST DO ONE FOR NOW!
	def register(){}
	
	//def newuser() {}
	
	def newusersession(){
		
		User usr = new User()
		usr.salt = getSalt()
		String password = params.password + usr.salt
		usr.passwordHash = password.hashCode() //hopefully this works?
		usr.email = params.email
		usr.firstName = params.firstName
		usr.lastName = params.lastName
		usr.phone = params.phone
		usr.notifyFrequency = params.frequency.toInteger()
		usr.save(flush:true)
		session["userid"] = usr.id
		redirect(controller:"profile")
	}
	
	private static String getSalt() 
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
	
	def postRegister(){}
	
	
}
