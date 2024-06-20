package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageDetailedSimpleDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageSimpleDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Named("simpleMessage")
    MessageSimpleDto messageToSimpleMessageDto(Message message);

    /**
     * This is necessary since the MessageSimpleDto misses the text property and the collection mapper can't handle
     * missing fields.
     **/
    @IterableMapping(qualifiedByName = "simpleMessage")
    List<MessageSimpleDto> messageToSimpleMessageDto(List<Message> message);

    MessageDetailedSimpleDto messageToDetailedMessageDto(Message message);

    Message detailedMessageDtoToMessage(MessageDetailedSimpleDto messageDetailedDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    Message messageInquiryDtoToMessage(MessageInquiryDto messageInquiryDto);

    MessageInquiryDto messageToMessageInquiryDto(Message message);
}

