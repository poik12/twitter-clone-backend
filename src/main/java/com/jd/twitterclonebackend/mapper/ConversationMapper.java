package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ConversationMapper {

    public ConversationEntity mapFromDtoToEntity(ConversationRequestDto conversationRequestDto,
                                                 UserEntity creatorEntity,
                                                 UserEntity participantEntity) {

        if (Objects.isNull(conversationRequestDto) ||
                Objects.isNull(creatorEntity) ||
                Objects.isNull(participantEntity)) {
            return null;
        }

        return ConversationEntity.builder()
                .creator(creatorEntity)
                .participant(participantEntity)
                .messages(Collections.emptyList())
                .latestMessageContent(null)
                .latestMessageRead(false)
                .latestMessageTime(Date.from(Instant.now()))
                .build();
        // TODO: do something with latestMessageRead and message content?
    }



    public ConversationResponseDto mapFromEntityToDto(ConversationEntity conversationEntity) {

        if (Objects.isNull(conversationEntity)) {
            return null;
        }

        return ConversationResponseDto.builder()
                .participantName(conversationEntity.getParticipant().getName())
                .participantUsername(conversationEntity.getParticipant().getUsername())
                .creatorName(conversationEntity.getCreator().getName())
                .creatorUsername(conversationEntity.getCreator().getUsername())
                .messages(
                        conversationEntity.getMessages()
                        .stream()
                        .map(this::mapFromEntityToMessageDto)
                        .toList()
                )
                .latestMessageContent(conversationEntity.getLatestMessageContent())
                .latestMessageRead(conversationEntity.getLatestMessageRead())
                .latestMessageTime(conversationEntity.getLatestMessageContent())
                .build();
        // TODO: relative time in messages and latest message
    }

    public MessageResponseDto mapFromEntityToMessageDto(MessageEntity messageEntity) {

        if (Objects.isNull(messageEntity)) {
            return null;
        }

        return MessageResponseDto.builder()
                .content(messageEntity.getContent())
                .senderId(messageEntity.getSender().getId())
                .recipientId(messageEntity.getRecipient().getId())
                .createdAt(messageEntity.getCreatedAt().toString())
                .build();
    }

}
