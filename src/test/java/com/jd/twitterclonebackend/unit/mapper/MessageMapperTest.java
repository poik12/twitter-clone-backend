package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.MessageMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageMapperTest extends UnitTestInitData {

    @InjectMocks
    private MessageMapper underTest;

    @Test
    @DisplayName(value = "Should map from Message Dto to Message Entity")
    void should_mapFromMessageDto_toMessageEntity() {
        // given
        UserEntity primeUser = initPrimeUser();
        UserEntity secondUser = initSecondUser();
        String message = "Test Message";
        ConversationEntity conversationEntity = initConversationEntity();

        // when
        var result = underTest.mapFromDtoToEntity(
                primeUser,
                secondUser,
                message,
                conversationEntity

        );

        // then
        assertAll(
                () -> {
                    assertEquals(message, result.getContent());
                    assertEquals(conversationEntity, result.getConversation());
                    assertEquals(primeUser, result.getSender());
                    assertEquals(secondUser, result.getRecipient());
                }
        );
    }

    @Test
    @DisplayName(value = "Should map from Message Entity to Message Dto")
    void should_mapFromMessageEntity_toMessageDto() {
        // given
        MessageEntity messageEntity = initMessageEntity();

        // when
        var result = underTest.mapFromEntityToDto(messageEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(messageEntity.getContent(), result.getContent());
                    assertEquals(messageEntity.getSender().getUsername(), result.getSenderUsername());
                    assertEquals(messageEntity.getRecipient().getUsername(), result.getRecipientUsername());
                }
        );
    }

}