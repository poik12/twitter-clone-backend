package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.ConversationController;
import com.jd.twitterclonebackend.controller.NotificationController;
import com.jd.twitterclonebackend.controller.handler.ConversationControllerExceptionHandler;
import com.jd.twitterclonebackend.controller.handler.NotificationControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.response.NotificationResponseDto;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.ConversationService;
import com.jd.twitterclonebackend.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public NotificationController notificationController;

    @MockBean
    public NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .setControllerAdvice(new NotificationControllerExceptionHandler())
                .build();
    }

    @Test
    void should_getAllNotifications() throws Exception {
        // given
        NotificationResponseDto notificationResponseDto = initNotificationResponseDto();

        List<NotificationResponseDto> notificationResponseDtoList = List.of(
                notificationResponseDto,
                notificationResponseDto,
                notificationResponseDto
        );

        // when then
        when(notificationService.getAllNotifications(any()))
                .thenReturn(notificationResponseDtoList);

        mockMvc.perform(
                        get("/notifications")
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(notificationResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(notificationResponseDto.getName()));
    }

    @Test
    void should_deleteNotification_byNotificationId() throws Exception {
        // given
        NotificationResponseDto notificationResponseDto = initNotificationResponseDto();

        // when then
        mockMvc.perform(
                        delete("/notifications/{notificationId}", notificationResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}