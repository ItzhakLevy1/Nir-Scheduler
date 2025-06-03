package com.nirSchedular.nirSchedularMongo.service.impl;

import com.nirSchedular.nirSchedularMongo.exception.OurException;
import com.nirSchedular.nirSchedularMongo.service.interfac.IEmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Implementation of IEmailService for sending emails using JavaMailSender.
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    /**
     * Constructor-based dependency injection of JavaMailSender.
     *
     * @param mailSender Spring mail sender bean configured in application properties.
     */
    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to); // recipient
            helper.setSubject(subject); // subject
            helper.setText(body, true); // true = enable HTML
            helper.setFrom("Gova.osh@gmail.com"); // sender email

            mailSender.send(mimeMessage); // send the HTML email

            logger.info("HTML email sent successfully to {}", to);

        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new OurException("Email could not be sent. Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while sending email to {}: {}", to, e.getMessage());
            throw new OurException("Unexpected error while sending email.");
        }
    }

}
