package com.arogya.stage.mail;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import com.arogya.stage.common.Constants;

public class ArogyaMail {

	public String to;
	public String from;

	public ArogyaMail() {

	}

	public boolean sendemail(String receipant) {
		// Recipient's email ID needs to be mentioned.
		to = receipant;

		// Sender's email ID needs to be mentioned

		String from = "arogya.sup@gmail.com";
		String host = "smtp.gmail.com";

		// String to = "reciveremail@xxxx.xxx";
		String subject = "Health Report Upload Notification";
		// String msg ="email text...."
		// final String from ="senderemail@gmail.com"
		final String password = "arogyasup";

		Transport transport = null;
		InternetAddress addressFrom = null;
		MimeMessage message = null;

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.debug", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		// session.setDebug(true);

		try {

			transport = session.getTransport();

		} catch (NoSuchProviderException e) {

			e.printStackTrace();
		}

		try {
			addressFrom = new InternetAddress(from);
			message = new MimeMessage(session);
			message.setSender(addressFrom);
			message.setSubject(subject);

			message.setContent(Constants.msgText, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			Transport.send(message);
			transport.close();
			transport.connect();

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

}
