package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.MessageService;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleMessageService implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MessageRepository messageRepository;
    private final EmailService emailService;

    public SimpleMessageService(MessageRepository messageRepository, EmailService emailService) {
        this.messageRepository = messageRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Message> findAll() {
        LOGGER.debug("Find all messages");
        return messageRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public Message findOne(Long id) {
        LOGGER.debug("Find message with id {}", id);
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        } else {
            throw new NotFoundException(String.format("Could not find message with id %s", id));
        }
    }

    @Override
    public Message publishMessage(Message message) {
        LOGGER.debug("Publish new message {}", message);
        message.setPublishedAt(LocalDateTime.now());
        /* just for testing purposes
        try {
            emailService.sendMessageUsingThymeleafTemplate("leon.stempfer@outlook.com", "New message", message.getText());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }*/
        return messageRepository.save(message);
    }

}
