package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
                .createdAt(getRelativeDate(messageEntity.getCreatedAt()))
                .build();
    }

    public String getRelativeDate(Date messageDate) {

        LocalDateTime todayDateTime = Instant.now()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime messageDateTime = messageDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (todayDateTime.getDayOfMonth() > messageDateTime.getDayOfMonth() ||
                ChronoUnit.DAYS.between(todayDateTime, messageDateTime) > 1 ) {

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return formatter.format(messageDate);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(messageDate);
    }

}
