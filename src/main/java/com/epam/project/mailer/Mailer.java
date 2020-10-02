package com.epam.project.mailer;

import java.util.Arrays;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mailer {
	private static final Logger log = LoggerFactory.getLogger(Mailer.class);
	private static String username;
	private static String password;
	private static String host;
	private static Properties props = new Properties();

	private Mailer() {
	}

	static {
		// Option 1 - app.properties
		try {
			ResourceBundle db = ResourceBundle.getBundle("app");
			username = db.getString("mailer.username");
			password = db.getString("mailer.password");
			host = db.getString("mailer.host");
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}

		props.setProperty("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.smtp.host", host);

		// Option 2 - JNDI (Not succeeded :()
//		Context envCtx;
//		try {
//			Context initCtx = new InitialContext();
//			envCtx = (Context) initCtx.lookup("java:comp/env");
//			session = (Session) envCtx.lookup("mail/Session");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
	}
	private static Session session = Session.getInstance(props);
	private static Message msg = new MimeMessage(session);

	public static void sendMail(String[] usernames, String text, String subject)
			throws MessagingException {
		Address[] recipients = new Address[usernames.length];
		for (int i = 0; i < usernames.length; i++) {
			recipients[i] = new InternetAddress(usernames[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, recipients);
		msg.setText(text);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		Transport.send(msg, username, password);
		log.info("Message sent to {}", Arrays.asList(recipients));
//		Transport.send(msg);
	}

}