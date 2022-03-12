package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.ConversationException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidConversationEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.ConversationMapper;
import com.jd.twitterclonebackend.mapper.MessageMapper;
import com.jd.twitterclonebackend.repository.ConversationRepository;
import com.jd.twitterclonebackend.repository.MessageRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public void createConversation(ConversationRequestDto conversationRequestDto) {

        UserEntity conversationCreator = userDetailsService.currentLoggedUserEntity();

        UserEntity conversationParticipant = userRepository
                .findByUsername(conversationRequestDto.getParticipantUsername())
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() +
                                conversationRequestDto.getParticipantUsername(),
                        HttpStatus.NOT_FOUND
                ));

        ConversationEntity conversationEntity = conversationMapper.mapFromDtoToEntity(
                conversationRequestDto,
                conversationCreator,
                conversationParticipant
        );

        conversationRepository.save(conversationEntity);
    }

    @Override
    public List<ConversationResponseDto> getAllConversations() {

        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();

        return conversationRepository
                .findAllByCreatorOrParticipant(
                        loggedUserEntity,
                        loggedUserEntity
                )
                .stream()
                .map(conversationMapper::mapFromEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void sendMessage(MessageRequestDto messageRequestDto) {

        UserEntity senderEntity = userDetailsService.currentLoggedUserEntity();

        ConversationEntity conversationEntity = conversationRepository
                .findById(messageRequestDto.getConversationId())
                .orElseThrow(() -> new ConversationException(
                        InvalidConversationEnum.CONVERSATION_NOT_FOUND_WITH_ID.getMessage()
                                + messageRequestDto.getConversationId(),
                        HttpStatus.NOT_FOUND
                ));

        String recipientUsername;
        if (!senderEntity.getUsername().equals(conversationEntity.getParticipant().getUsername())) {
            recipientUsername = conversationEntity.getParticipant().getUsername();
        } else {
            recipientUsername = conversationEntity.getCreator().getUsername();
        }
        String finalRecipientUsername = recipientUsername;

        UserEntity recipientEntity = userRepository
                .findByUsername(finalRecipientUsername)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + finalRecipientUsername,
                        HttpStatus.NOT_FOUND
                ));

        // todo: empty string in conversation as last message
        // todo: sending massages check
        // todo: add to conversation in user profile?
        // todo: update like post for post entity
        // todo: in userprofile add retweets (comment list) and likes
        // todo: add delete comment

        MessageEntity messageEntity = messageMapper.mapFromDtoToEntity(
                senderEntity,
                recipientEntity,
                messageRequestDto.getContent(),
                conversationEntity
        );
        MessageEntity savedMessageEntity = messageRepository.save(messageEntity);

        // todo: try to write it better
        // update conversation params, last massage has only 25 characters
        String latestMessageContent = savedMessageEntity.getContent();
        if (latestMessageContent.length() > 25) {
            latestMessageContent = savedMessageEntity.getContent().substring(0, 25) + "...";
        }

        conversationRepository.updateLatestMessageContentAndTime(
                conversationEntity.getId(),
                latestMessageContent,
                Date.from(Instant.now())
        );

    }

    @Override
    public ConversationResponseDto getConversationById(Long conversationId) {
        ConversationEntity conversationEntity = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ConversationException(
                        InvalidConversationEnum.CONVERSATION_NOT_FOUND_WITH_ID.getMessage() + conversationId,
                        HttpStatus.NOT_FOUND
                ));
        return conversationMapper.mapFromEntityToDto(conversationEntity);
    }
}
