package hudson
import javax.mail.*
import javax.mail.internet.*
import javax.media.j3d.Switch;

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.mail.*
import org.springframework.activation.*
import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import grails.util.Environment
import groovy.hudson.Notification
import hudson.Query

import java.net.URL;

import org.jsoup.Jsoup;

class User {

	String email
	String phone
	String passwordHash
	String salt
	String firstName
	String lastName
	Integer notifyFrequency
	Integer carrier

	enum Carrier {
		ATT(1), TMOBILE(2), VIRGIN(3), CINGULAR(4), SPRINT(5),
		VERIZON(6), NEXTEL(7)

		private Carrier(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		private final int value;
	}

	static hasMany = [queries: Query]

	static constraints = {
		email email: true, blank: false
		phone nullable: true, blank: false
		carrier nullable: true
		passwordHash blank: false
		salt blank: false
		firstName blank: false
		lastName blank: false
		notifyFrequency blank: false
	}

	String phoneEmail(String phone) {
		switch (carrier) {
			case Carrier.ATT.getValue():
				return phone + "@txt.att.net";
			case Carrier.TMOBILE.getValue():
				return phone + "@tmomail.net";
			case Carrier.VIRGIN.getValue():
				return phone + "@vmobl.com";
			case Carrier.CINGULAR.getValue():
				return phone + "@cingularme.com";
			case Carrier.SPRINT.getValue():
				return phone + "@messaging.sprintpcs.com";
			case Carrier.VERIZON.getValue():
				return phone + "@vtext.com";
			case Carrier.NEXTEL.getValue():
				return phone + "@messaging.nextel.com";
			default:
				return null;
		}
	}

	public void sendPassword (String to, String recFirstName, String link) {
		// Sender's email ID needs to be mentioned
		String from = "Hudson";

		// Get system properties
		Properties properties = System.getProperties();

		properties.setProperty ("mail.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "" + 587);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty ("mail.transport.protocol", "smtp");

		Session session = Session.getInstance(properties, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("hudson.no.reply.please", "TeamHudson9");
					}
				});

		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));

		// Set To: header field of the header.
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// Set Subject: header field
		message.setSubject("Hudson: Temporary Password");

		// Now set the actual message
		message.setText("Hey, " + recFirstName + ", log in with this temporary password. You can reset your password once logged into Hudson: " + link);

		Transport.send(message);
	}

	void sendNotification(String to, String messageBody, Boolean autoReply) {
		// Sender's email ID needs to be mentioned
		String from = "Hudson";

		// Get system properties
		Properties properties = System.getProperties();

		properties.setProperty ("mail.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "" + 587);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty ("mail.transport.protocol", "smtp");

		Session session = Session.getInstance(properties, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("hudson.no.reply.please", "TeamHudson9");
					}
				});

		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));

		// Set To: header field of the header.
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		if (autoReply) {
			//TODO: Subject
			message.setReplyTo([new InternetAddress(this.email, this.firstName)]);
		} else {
			// Set Subject: header field
			message.setSubject("Hudson: New Post!");
		}
		// Now set the actual message
		message.setText(messageBody);

		Transport.send(message);
	}

	String getReplyEmail(String link) {
		System.out.println(link);
		Document doc = Jsoup.connect(link).get();
		Element replyLinkElem = doc.select("#replylink").first();
		if (replyLinkElem != null) {
			System.out.println(replyLinkElem);
			String linkHead = "http://sfbay.craigslist.org";
			String replyLink = linkHead + replyLinkElem.attr("href");
			System.out.println(replyLink);
			Document replyDoc = Jsoup.connect(replyLink).get();
			Elements replyEmail = replyDoc.select(".anonemail").first();
			if (replyEmail != null) {
				String replyEmailAddr = replyEmail.attr("value");
				return replyEmailAddr;
			}
		}
		return "";
	}

	void notifyUserOfReplyIssue(String link) {
		//sendNotification(this.email, "There was an issue auto-replying to the following post: " +
		//		link + " Please check out the original listing.", false);
		Notification n = new Notification(this.email, this.email, "There was an issue auto-replying to the following post: " +
				link + " Please check out the original listing.", "Hudson: Auto-reply error")
		n.enqueue()
	}

	void notifyUser() {
		ArrayList<String> linksToSend = new ArrayList<String>();
		for (Query nextQuery : this.queries) {
			if (!nextQuery.notify) continue;
			for (Post nextPost : nextQuery.posts) {
				if (nextPost.isNew) {
					nextPost.isNew = false;
					linksToSend.add(nextPost.link);
					if (nextPost.replyEmail.isEmpty() && nextQuery.instantReply) {
						String email = getReplyEmail(nextPost.link);
						nextPost.replyEmail = email;
						if (email == "") notifyUserOfReplyIssue(nextPost.link);
						if (Environment.current.equals(Environment.PRODUCTION)) {
							Notification n = new Notification(nextPost.replyEmail, this.email, nextQuery.responseMessage, "Your post on Craigslist");
							n.enqueue();
						}
							//sendNotification(nextPost.replyEmail, nextQuery.responseMessage, true);
					} else if (!nextPost.replyEmail.isEmpty() && nextQuery.instantReply) {
						if (Environment.current.equals(Environment.PRODUCTION)) {
							Notification n = new Notification(nextPost.replyEmail, this.email, nextQuery.responseMessage, "Your post on Craigslist");
							n.enqueue();
						}
							//sendNotification(nextPost.replyEmail, nextQuery.responseMessage, true);
					}
				}
			}
		}

		String allLinks = "";
		boolean isFirst = true;
		for (String nextLink : linksToSend) {
			if (!isFirst) allLinks += "\n ";
			if (isFirst) isFirst = false;
			allLinks += nextLink;
		}

		String messageBody = "Hey " + this.firstName + ", \nCheck out these new Craigslist listings " +
				" that match your criteria: \n" + allLinks;

		if (!allLinks.isEmpty()) {
			if (!this.email.isEmpty()) {
				Notification n = new Notification(this.email, this.email, messageBody, "Hudson: New Posts!");
				n.enqueue();
			}
				//sendNotification(this.email, messageBody, false);
			if (this.phone != null && !this.phone.isEmpty() && Environment.current.equals(Environment.PRODUCTION)) {
				String newPhoneEmail = phoneEmail(this.phone);
				Notification n = new Notification(newPhoneEmail, newPhoneEmail, messageBody, "Hudson: New Posts!");
				n.enqueue();
				//sendNotification(newPhoneEmail, messageBody, false);
			}
		}

	}
}

