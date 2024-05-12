package at.ac.tuwien.sepr.groupphase.backend.service.Mail;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);


    void sendMessageUsingThymeleafTemplate(String to,
                                           String subject,
                                           String text)
        throws IOException, MessagingException;
}