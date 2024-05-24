package at.ac.tuwien.sepr.groupphase.backend.service.mail;

import jakarta.mail.MessagingException;

import java.util.Map;

/**
 * Service for sending emails.
 */
public interface EmailService {

    /**
     * Sends an email using a Thymeleaf template.
     *
     * @param to            recipient email address
     * @param subject       email subject
     * @param templateModel map for the Thymeleaf template
     * @throws MessagingException if the email cannot be sent
     */
    void sendMessageUsingThymeleafTemplate(String to,
                                           String subject,
                                           Map<String, Object> templateModel)
        throws MessagingException;


    /**
     * Sends a cancellation email using a Thymeleaf template.
     *
     * @param to            recipient email address
     * @param subject       email subject
     * @param templateModel map for the Thymeleaf template
     * @throws MessagingException if the email cannot be sent
     */
    void sendCancellationMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}