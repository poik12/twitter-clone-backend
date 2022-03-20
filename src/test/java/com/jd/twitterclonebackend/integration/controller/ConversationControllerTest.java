package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.ConversationController;
import com.jd.twitterclonebackend.controller.handler.ConversationControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConversationControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public ConversationController conversationController;

    @MockBean
    public ConversationService conversationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(conversationController)
                .setControllerAdvice(new ConversationControllerExceptionHandler())
                .build();
    }

    @Test
    void should_createConversation() throws Exception {
        // given
        ConversationRequestDto conversationRequestDto = initConversationRequestDto();

        // when then
        mockMvc.perform(
                        post("/conversations")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(conversationRequestDto))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_getAllConversations() throws Exception {
        // given
        ConversationResponseDto conversationResponseDto = initConversationResponseDto();
        List<ConversationResponseDto> conversationResponseDtoList = List.of(
                conversationResponseDto,
                conversationResponseDto,
                conversationResponseDto
        );

        // when then
        when(conversationService.getAllConversations(any()))
                .thenReturn(conversationResponseDtoList);


        mockMvc.perform(
                        get("/conversations")
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].participantUsername")
                        .value(conversationResponseDto.getParticipantUsername()))
                .andExpect(jsonPath("$[0].creatorUsername")
                        .value(conversationResponseDto.getCreatorUsername()));
    }

    @Test
    void should_getConversation_byId() throws Exception {
        // given
        ConversationResponseDto conversationResponseDto = initConversationResponseDto();

        // when then
        when(conversationService.getConversationById(any()))
                .thenReturn(conversationResponseDto);

        mockMvc.perform(
                        get("/conversations/{conversationId}", conversationResponseDto.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantUsername")
                        .value(conversationResponseDto.getParticipantUsername()))
                .andExpect(jsonPath("$.creatorUsername")
                        .value(conversationResponseDto.getCreatorUsername()));
    }

    @Test
    void should_sendMessage() throws Exception {
        // given
        MessageRequestDto messageRequestDto = initMessageRequestDto();
        MessageResponseDto messageResponseDto = initMessageResponseDto();

        // when then
        when(conversationService.sendMessage(messageRequestDto))
                .thenReturn(messageResponseDto);

        mockMvc.perform( post("/conversations/messages")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(messageRequestDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content")
                        .value(messageResponseDto.getContent()));
    }


    @Test
    void should_getMessagesForConversation_byConversationId() throws Exception {
        // given
        MessageResponseDto messageResponseDto = initMessageResponseDto();

        List<MessageResponseDto> messageResponseDtoList = List.of(
                messageResponseDto,
                messageResponseDto,
                messageResponseDto
        );

        // when then
        when(conversationService.getMessagesForConversationById(any(), any()))
                .thenReturn(messageResponseDtoList);

        mockMvc.perform(
                        get("/conversations/messages/{conversationId}", 1L)
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].recipientUsername")
                        .value(messageResponseDto.getRecipientUsername()))
                .andExpect(jsonPath("$[0].senderUsername")
                        .value(messageResponseDto.getSenderUsername()));
    }
}