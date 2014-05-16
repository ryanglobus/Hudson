package home

import HudsonJobs.*
import hudson.User
import java.security.MessageDigest
import java.security.SecureRandom
import javax.xml.bind.DatatypeConverter
import hudson.Post

class HomeController {

	private static final Random RANDOM = new SecureRandom();
	public static final int PASSWORD_LENGTH = 8;
	
    def index() {}
	
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
		if(User.findByEmail(params.email) != null) {
			flash.message = "A user already exists with this email address. Please try again with alternate email."
			redirect(action:'register')
			return
		}
		if(params.password != params.confirmPassword) {
			flash.message = "Your passwords did not match. Try again!"
			redirect(action:'register')
			return
		}
		User usr = new User()
		usr.salt = getSalt()		
		usr.passwordHash = getHashedPassword(params.password, usr.salt)
		usr.email = params.email
		usr.firstName = params.firstName
		usr.lastName = params.lastName
		usr.phone = params?.phone
		usr.notifyFrequency = params.frequency.toInteger()
		usr.carrier = User.Carrier.valueOf(params.carrier).getValue()
<<<<<<< HEAD
		usr.save(flush:true, failOnError: true)
=======
		usr.save(flush:true, failOnError:true)
>>>>>>> master
		session["userid"] = usr.id
		
		def frequencyInMilliseconds = usr.notifyFrequency * 60000
		NotifyJob.schedule(frequencyInMilliseconds, -1, [user:usr]) //we want notifications to run forever!
			
		redirect(controller:"profile")
	}
	
	public static String getHashedPassword(String pass, String salt) {
		String password = pass + salt
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256")
		byte[] hash = digest.digest(password.getBytes("UTF-8"))
		return DatatypeConverter.printHexBinary(hash);
	}
	

	public static String getSalt() 
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[32];
		sr.nextBytes(salt);
		return salt.toString();
	}

	def postRegister(){}
	
	def forgotPassword() {}
		
	def sendPassword() {
		boolean validForm
		withForm {
			validForm = true
		} .invalidToken {
			flash.message = "Login failed. Please try again."
			redirect(action:'forgotPassword')
			validForm = false
		}
		if(!validForm) return
		if(params.username.length() == 0) {
			flash.message = "Please enter a valid email address"
			redirect(action:'forgotPassword')
			return
		}
		User usr = User?.findByEmail(params.username)
		if(usr == null) {
			flash.message = "Please enter a valid email address"
			redirect(action:'forgotPassword')	
			return
		}
		String temporary = getTemporaryPassword()
		usr.salt = getSalt()
		usr.passwordHash = getHashedPassword(temporary, usr.salt)
		usr.sendPassword(usr.email, usr.firstName, temporary)
		usr.save(flush:true, failOnError:true)
		flash.message = "An email has been sent with a temporary password. You may change it to a password of your choosing when you log in."
		redirect(action:'index')
	}
	
	private String getTemporaryPassword() {
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
		
		String pw = "";
		for (int i=0; i<PASSWORD_LENGTH; i++) {
			int index = (int)(RANDOM.nextDouble()*letters.length());
			pw += letters.substring(index, index+1);
		}
		return pw;
	}
		
}
