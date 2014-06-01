package groovy.hudson
import groovy.hudson.queue.*;
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

import java.net.URL;

import org.jsoup.Jsoup;

class Notification {
	public String rec;
	public String replyEmail;
	public String body;
	public String subject;

	static final Queue<Notification> queue = new Queue<Notification>(queueName(), getConverter())
	
	private static StringConverter<Notification> getConverter() {
		return new StringConverter<Notification>() {
				
			String generateString(Notification n) {
				return n.rec + '\n' + n.replyEmail + '\n' + n.subject + '\n' + n.body;
			}
				
			Notification parseString(String message) {
				//substring(i, j) includes char at i and excludes char at j
				
				//Email address or phone number
				int endOfRec = message.indexOf('\n');
				if (endOfRec < 0) return null
				String rec = message.substring(0, endOfRec);
				
				//Reply email
				int endOfReply = message.indexOf('\n', endOfRec+1);
				if (endOfReply < 0) return null
				String replyEmail = message.substring(endOfRec+1, endOfReply);

				//Subject
				int endOfSub = message.indexOf('\n', endOfReply+1);
				if (endOfSub < 0) return null
				String subject = message.substring(endOfReply+1,endOfSub);
				
				//List of links with message
				String body = message.substring(endOfSub+1);
				
				return new Notification(rec, replyEmail, body, subject);
			}
		}
	}

	private static String queueName() {
        if (Environment.current.equals(Environment.PRODUCTION)) {
            return 'notification'
        } else {
            // create a timestamped query for each dev/test run to ensure
            // starting with an empty queue
            return 'notification' + Long.toString(System.currentTimeMillis())
        }
    }
	
	public Notification(String rec, String replyEmail, String body, String subject) {
		this.rec = rec;
		this.replyEmail = replyEmail;
		this.body = body;
		this.subject = subject;
	}

	public void sendNotification() {
		// Sender's email ID needs to be mentioned
		String from = "Hudson";

		// Get system properties
		Properties properties = System.getProperties();

		properties.setProperty ("mail.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "" + 587);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.transport.protocol", "smtp");

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
		message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(rec));
		message.setReplyTo([new InternetAddress(replyEmail)].toArray(new Address[1]));
		// Set Subject: header field
		message.setSubject(subject);
			
		// Now set the actual message
		message.setText(body);

		Transport.send(message);

	}
	 
	public void enqueue() {
		groovy.hudson.queue.Message<Notification> msg = new groovy.hudson.queue.Message<Notification>(this)
		queue.enqueue(msg)
	}
}
