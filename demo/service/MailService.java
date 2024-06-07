package com.example.demo.service;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;


@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    protected void sendEmail(String recipient, String content) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("in authentication");
                return new PasswordAuthentication("ibtissamhadik10@gmail.com", "bxja nvvh vazz gqpe");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("ibtissamhadik10@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        msg.setSubject("Jibi Verification");
        msg.setContent(content, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }

}
