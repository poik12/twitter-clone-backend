package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationService {

    void createConversation(ConversationRequestDto conversationRequestDto);

    List<ConversationResponseDto> getAllConversations(Pageable pageable);

    MessageResponseDto sendMessage(MessageRequestDto messageRequestDto);

    ConversationResponseDto getConversationById(Long conversationId);

    List<MessageResponseDto> getMessagesForConversationById(Long conversationId, Pageable pageable);
}
