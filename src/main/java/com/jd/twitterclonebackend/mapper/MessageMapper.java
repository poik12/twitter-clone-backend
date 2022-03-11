package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MessageMapper {

    public MessageEntity mapFromDtoToEntity(MessageRequestDto messageRequestDto, ConversationEntity conversationEntity) {

        if (Objects.isNull(messageRequestDto) ||
                Objects.isNull(conversationEntity)
        ) {
            return null;
        }

        return MessageEntity.builder()
                .content(messageRequestDto.getContent())
                .conversation(conversationEntity)
                .sender(conversationEntity.getCreator())
                .recipient(conversationEntity.getParticipant())
                .build();
    }

    public MessageResponseDto mapFromEntityToDto(MessageEntity messageEntity) {

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
