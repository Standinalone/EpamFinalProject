package com.epam.project.mailer;

import java.util.Date;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.epam.project.constants.Constants;

public class Mailer {
	private static String username ;
	private static String password;
	private static String host;
	private static Properties props = new Properties();

	private Mailer() {
	}

	static {
		try {
		ResourceBundle db = ResourceBundle.getBundle(Constants.PROPS_FILE);
		username = db.getString("mailer.username");
		password = db.getString("mailer.password");
		host = db.getString("mailer.host");
	} catch (MissingResourceException e) {
		e.printStackTrace();
	}
		
		props.setProperty("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.smtp.host", host);
		
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
			throws AddressException, MessagingException {
		Address[] recipients = new Address[usernames.length];
		for (int i = 0; i < usernames.length; i++) {
			recipients[i] = new InternetAddress(usernames[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, recipients);
		msg.setText(text);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		Transport.send(msg, username, password);
//		Transport.send(msg);
	}

}