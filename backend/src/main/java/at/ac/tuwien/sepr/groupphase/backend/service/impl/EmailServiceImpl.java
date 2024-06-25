package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private org.springframework.mail.javamail.JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("mail-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    public void sendCancellationMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("cancellation-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    public void sendUpdateMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("update-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }


    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        if (to.endsWith("@example.com")) {
            //to = "spottersepm@gmail.com";
            to = "f3lix.weilharter@gmail.com";
        }
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("sepr.spotter@outlook.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

    @Override
    public void sendPermanentDeletionMessageUsingThymeleafTemplate(PermanentReservation permanentReservation,
                                                                   List<LocalDate> deletedReservationDates, Map<String, Object> templateModel) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("permanent-deletion-template.html", thymeleafContext);

        sendHtmlMessage(permanentReservation.getApplicationUser().getEmail(), "Cancellation confirmation for permanent reservation", htmlBody);
    }

    @Override
    public void sendPermanentConfirmationMessageUsingThymeleafTemplate(PermanentReservation permanentReservation,
                                                                       List<LocalDate> conflictedDates, Map<String, Object> templateModel) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("permanent-confirmation-template.html", thymeleafContext);

        sendHtmlMessage(permanentReservation.getApplicationUser().getEmail(), "Confirmation for permanent reservation", htmlBody);
    }

}
