package hudson
import javax.mail.*
import javax.mail.internet.*
import javax.media.j3d.Switch;

import org.springframework.mail.*
import org.springframework.activation.*
import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

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
        passwordHash blank: false
        salt blank: false
        firstName blank: false
        lastName blank: false
        notifyFrequency blank: false
    }

    void sendNotification(String to, String recFirstName, String link) {
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
        message.setSubject("Hudson: New Post!");

        // Now set the actual message
        message.setText("Hey, " + recFirstName + "! Check out this awesome new listing that matches your criteria: " + link);

        Transport.send(message);
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

    void notifyUser() {
        // below two lines are for testing
        //sendNotification("ckortel@stanford.edu", "Kelly", "www.NEWPOST.com");
        //sendNotification("4108977488@txt.att.net", "Kelly", "www.NEWPOST.com");
        for (Query nextQuery : this.queries) {
            if (!nextQuery.instantReply) continue;
            for (Post nextPost : nextQuery.posts) {
                if (nextPost.isNew) {
                    nextPost.isNew = false;
                    if (!this.email.isEmpty())
                        sendNotification(this.email, this.firstName, nextPost.link);
                    if (!this.phone.isEmpty()) {
                        String newPhoneEmail = phoneEmail(this.phone);
                        sendNotification(newPhoneEmail, this.firstName, nextPost.link);
                    }
                }
            }
        }
    }

}