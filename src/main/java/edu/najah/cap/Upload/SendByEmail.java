package edu.najah.cap.Upload;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.Properties;


public class SendByEmail implements IUploadData {

    public void Send(String receiver) throws IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("8a4f6b83bc8178", "4ffa0d5ed69dfe");
            }
        });
        try {
            // Create a MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("yousefnajeh03@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("yousefnajeh3@gmail.com"));
            message.setSubject("the data you requsted from user data project");
            // Create the text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("hi this user data app this is the data you requsted");

            // Create the attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile("king/pdf_files/newdataa/ZipFiles/new.zip");

            // Create the multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            // Set the multipart as the content of the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
        } catch (Exception e) {
            e.getMessage();
        }


//        System.out.println("Email sent successfully!");
//
//        Email email = EmailBuilder.startingBlank()
//                .from("From", "yousefnajeh3@gmail.com")
//                .to("1 st Receiver", receiver)
//                .withSubject("Email Subject")
//                .withPlainText("Email Body")
//                .buildEmail();
//
//        Mailer mailer = MailerBuilder
//                .withSMTPServer("live.smtp.mailtrap.io", 587 , "api", "6a93448c394756bb4c10ac54ba374e0e")
//                .withTransportStrategy(TransportStrategy.SMTPS).buildMailer();
//
//        mailer.sendMail(email);
//    }
    }
}
