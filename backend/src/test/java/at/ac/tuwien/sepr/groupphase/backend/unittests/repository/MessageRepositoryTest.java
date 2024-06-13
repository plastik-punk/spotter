package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only entity beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class MessageRepositoryTest implements TestData {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @Transactional
    public void givenNothing_whenFindAllByOrderByPublishedAtDesc_thenReturnOrdered() {
        Message message1 = messageRepository.save(TEST_MESSAGE_1);
        Message message2 = messageRepository.save(TEST_MESSAGE_3);

        assertAll(
            () -> assertEquals(2, messageRepository.findAllByOrderByPublishedAtDesc().size()),
            () -> assertEquals(message2, messageRepository.findAllByOrderByPublishedAtDesc().get(0)),
            () -> assertEquals(message1, messageRepository.findAllByOrderByPublishedAtDesc().get(1))
        );
    }
}