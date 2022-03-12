package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;

import java.util.List;

public interface ConversationService {

    void createConversation(ConversationRequestDto conversationRequestDto);

    List<ConversationResponseDto> getAllConversations();

    void sendMessage(MessageRequestDto messageRequestDto);

    ConversationResponseDto getConversationById(Long conversationId);
}
