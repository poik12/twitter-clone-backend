package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.ConversationMapper;
import com.jd.twitterclonebackend.mapper.MessageMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ConversationMapperTest extends UnitTestInitData {

    @InjectMocks
    private ConversationMapper underTest;

    @Mock
    private MessageMapper messageMapper;

    @Test
    @DisplayName(value = "Should map from Conversation Dto to Conversation Entity")
    void should_mapFromConversationDto_toConversationEntity() {
        // given
        UserEntity primeUser = initPrimeUser();
        UserEntity secondUser = initSecondUser();
        ConversationRequestDto conversationRequestDto = initConversationRequestDto();

        // when
        var result = underTest.mapFromDtoToEntity(
                conversationRequestDto,
                primeUser,
                secondUser
        );

        // then
        assertAll(
                () -> {
                    assertEquals(primeUser, result.getCreator());
                    assertEquals(secondUser, result.getParticipant());
                    assertEquals(Collections.emptyList(), result.getMessages());
                    assertNull(result.getLatestMessageContent());
                    assertFalse(result.getLatestMessageRead());
                }
        );
    }

    @Test
    @DisplayName(value = "Should map from Conversation Entity to Conversation Dto")
    void should_mapFromConversationEntity_toConversationDto() {
        // given
        ConversationEntity conversationEntity = initConversationEntity();

        // when
        var result = underTest.mapFromEntityToDto(conversationEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(conversationEntity.getId(), result.getId());
                    assertEquals(conversationEntity.getCreator().getUsername(), result.getCreatorUsername());
                    assertEquals(conversationEntity.getCreator().getName(), result.getCreatorName());
                    assertEquals(conversationEntity.getCreator().getProfilePicture(), result.getCreatorProfilePicture());
                    assertEquals(conversationEntity.getParticipant().getUsername(), result.getParticipantUsername());
                    assertEquals(conversationEntity.getParticipant().getName(), result.getParticipantName());
                    assertEquals(conversationEntity.getParticipant().getProfilePicture(), result.getParticipantProfilePicture());
                    assertEquals(conversationEntity.getLatestMessageContent(), result.getLatestMessageContent());
                    assertEquals(conversationEntity.getLatestMessageRead(), result.getLatestMessageRead());
                }
        );
    }

}