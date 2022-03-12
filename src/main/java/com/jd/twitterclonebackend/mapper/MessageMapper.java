package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MessageMapper {

    public MessageEntity mapFromDtoToEntity(UserEntity senderEntity,
                                            UserEntity recipientEntity,
                                            String messageContent,
                                            ConversationEntity conversationEntity) {

        if (Objects.isNull(senderEntity) ||
                Objects.isNull(recipientEntity) ||
                Objects.isNull(messageContent) ||
                Objects.isNull(conversationEntity)
        ) {
            return null;
        }

        return MessageEntity.builder()
                .content(messageContent)
                .conversation(conversationEntity)
                .sender(senderEntity)
                .recipient(recipientEntity)
                .build();
    }

    public MessageResponseDto mapFromEntityToDto(MessageEntity messageEntity) {

        if (Objects.isNull(messageEntity)) {
            return null;
        }

        return MessageResponseDto.builder()
                .content(messageEntity.getContent())
                .senderUsername(messageEntity.getSender().getUsername())
                .recipientUsername(messageEntity.getRecipient().getUsername())
                .createdAt(messageEntity.getCreatedAt().toString())
                .build();
    }

}
