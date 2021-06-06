package com.bootcamp.registration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    public void sendEmail(String recipientEmail, String link_to_reset_password) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("testuserat@gmail.com","Login Support");
        helper.setTo(recipientEmail);

        String subject = "Your Registration to the site is successful.";
        String content = getMailContent(link_to_reset_password);

        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
    }

    private String getMailContent(String link) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<p> Hello, <p>").append("<br> Your registration to the site is successful.")
                .append("Click on the link below to confirm your registration.").append("<br> <a href=\""+link+"\" reset password.");
        return stringBuilder.toString();
    }
}
