package com.nirSchedular.nirSchedularMongo.service.interfac;

/**
 * Email service interface for sending emails.
 */
public interface IEmailService {

    /**
     * Sends an email.
     *
     * @param to Recipient email address
     * @param subject Subject of the email
     * @param body Body text of the email
     */
    void sendEmail(String to, String subject, String body);
}
