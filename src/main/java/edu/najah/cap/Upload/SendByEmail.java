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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("yousefnajeh.me"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("yousefnajeh3@gmail.com"));
            message.setSubject("the data you requsted from user data project");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("hi this user data app this is the data you requsted");
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile("king/pdf_files/ZipFiles/yousef.zip");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
