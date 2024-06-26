package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.util.List;
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

    /**
     * Sends an update email using a Thymeleaf template.
     *
     * @param to            recipient email address
     * @param subject       email subject
     * @param templateModel map for the Thymeleaf template
     * @throws MessagingException if the email cannot be sent
     */
    void sendUpdateMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException;

    /**
     * Sends an confirmation about deletion of a permanent reservation.
     *
     * @param permanentReservation    details for the email
     * @param deletedReservationDates dates which were deleted.
     * @param templateModel           model to use for the email
     */
    void sendPermanentDeletionMessageUsingThymeleafTemplate(PermanentReservation permanentReservation, List<LocalDate> deletedReservationDates, Map<String, Object> templateModel) throws MessagingException;

    /**
     * Sends an confirmation of a permanent reservation.
     *
     * @param permanentReservation details for the email
     * @param conflictedDates      dates which couldn't be booked.
     * @param templateModel        model to use for the email
     */
    void sendPermanentConfirmationMessageUsingThymeleafTemplate(PermanentReservation permanentReservation, List<LocalDate> conflictedDates, Map<String, Object> templateModel) throws MessagingException;


}