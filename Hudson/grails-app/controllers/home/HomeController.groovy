package home

import HudsonJobs.*
import hudson.User
import java.security.MessageDigest
import java.security.SecureRandom
import javax.xml.bind.DatatypeConverter


class HomeController {

    def index() { 
		
	}
	
	def login() {
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
		session["userid"] = user.id
		redirect(controller:"profile")
		
	}
	
	def register(){}
		
	def newusersession(){		
		User usr = new User()
		usr.salt = getSalt()
		
		usr.passwordHash = getHashedPassword(params.password, usr.salt) //hopefully this works?
		usr.email = params.email
		usr.firstName = params.firstName
		usr.lastName = params.lastName
		usr.phone = params.phone
		usr.notifyFrequency = params.frequency.toInteger()
		usr.carrier = User.Carrier.valueOf(params.carrier).getValue()
		usr.save(flush:true)
		session["userid"] = usr.id
		
		//Create the job that notifies the user!
		def frequencyInMilliseconds = usr.notifyFrequency * 60000
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
