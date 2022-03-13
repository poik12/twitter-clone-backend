package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    public final MessageMapper messageMapper;

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
    }

    public ConversationResponseDto mapFromEntityToDto(ConversationEntity conversationEntity) {

        if (Objects.isNull(conversationEntity)) {
            return null;
        }

        return ConversationResponseDto.builder()
                .id(conversationEntity.getId())
                .participantName(conversationEntity.getParticipant().getName())
                .participantUsername(conversationEntity.getParticipant().getUsername())
                .participantProfilePicture(conversationEntity.getParticipant().getProfilePicture())
                .creatorName(conversationEntity.getCreator().getName())
                .creatorUsername(conversationEntity.getCreator().getUsername())
                .creatorProfilePicture(conversationEntity.getCreator().getProfilePicture())
                .messages(
                        conversationEntity.getMessages()
                        .stream()
                        .map(messageMapper::mapFromEntityToDto)
                        .toList()
                )
                .latestMessageContent(conversationEntity.getLatestMessageContent())
                .latestMessageRead(conversationEntity.getLatestMessageRead())
                .latestMessageTime(messageMapper.getRelativeDate(conversationEntity.getLatestMessageTime()))
                .build();
    }

}
