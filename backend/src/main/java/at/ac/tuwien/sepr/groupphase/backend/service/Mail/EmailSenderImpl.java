package at.ac.tuwien.sepr.groupphase.backend.service.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service("EmailService")
public class EmailSenderImpl implements EmailService {

    @Autowired
    private org.springframework.mail.javamail.JavaMailSender emailSender;
    @Autowired
    private SimpleMailMessage template;
    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    @Value("classpath:images/logo.png")
    private Resource resourceFile;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sepr.spotter@outlook.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace(); //TODO: log error
        }
    }


    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, String text) throws MessagingException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", to); //TODO: change to user name
        templateModel.put("text", text); //TODO: change to message text
        templateModel.put("persons", "-Number of persons-"); //TODO: change to number of persons
        templateModel.put("restauranteName", "-Restaurant name-"); //TODO: change to restaurant name
        templateModel.put("reservationDate", "-Reservation date-"); //TODO: change to reservation date
        templateModel.put("reservationTime", "-Reservation time-"); //TODO: change to reservation time
        templateModel.put("link", "-link here-"); //TODO: change to the link
        templateModel.put("SpotterLogo", resourceFile);


        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process("mail-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("sepr.spotter@outlook.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        //helper.addInline("attachment.png", resourceFile);
        emailSender.send(message);
    }
}
