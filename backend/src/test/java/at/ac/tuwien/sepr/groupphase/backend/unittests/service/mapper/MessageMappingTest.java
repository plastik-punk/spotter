package at.ac.tuwien.sepr.groupphase.backend.unittests.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageDetailedSimpleDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageSimpleDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.MessageMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessageMappingTest implements TestData {

    private final Message message = Message.MessageBuilder.aMessage()
        .withId(TEST_MESSAGE_ID)
        .withTitle(TEST_NEWS_TITLE)
        .withSummary(TEST_NEWS_SUMMARY)
        .withText(TEST_NEWS_TEXT)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();
    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void givenNothing_whenMapDetailedMessageDtoToEntity_thenEntityHasAllProperties() {
        MessageDetailedSimpleDto messageDetailedDto = messageMapper.messageToDetailedMessageDto(message);
        assertAll(
            () -> assertEquals(TEST_MESSAGE_ID, messageDetailedDto.getId()),
            () -> assertEquals(TEST_NEWS_TITLE, messageDetailedDto.getTitle()),
            () -> assertEquals(TEST_NEWS_SUMMARY, messageDetailedDto.getSummary()),
            () -> assertEquals(TEST_NEWS_TEXT, messageDetailedDto.getText()),
            () -> assertEquals(TEST_NEWS_PUBLISHED_AT, messageDetailedDto.getPublishedAt())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoMessageEntitiesToSimpleDto_thenGetListWithSizeTwoAndAllProperties() {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        messages.add(message);

        List<MessageSimpleDto> messageSimpleDtos = messageMapper.messageToSimpleMessageDto(messages);
        assertEquals(2, messageSimpleDtos.size());
        MessageSimpleDto messageSimpleDto = messageSimpleDtos.get(0);
        assertAll(
            () -> assertEquals(TEST_MESSAGE_ID, messageSimpleDto.getId()),
            () -> assertEquals(TEST_NEWS_TITLE, messageSimpleDto.getTitle()),
            () -> assertEquals(TEST_NEWS_SUMMARY, messageSimpleDto.getSummary()),
            () -> assertEquals(TEST_NEWS_PUBLISHED_AT, messageSimpleDto.getPublishedAt())
        );
    }

    // TODO: test remaining methods

}