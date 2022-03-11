package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "/messages")
@RequiredArgsConstructor
@ApiRestController
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping()
    public ResponseEntity<Void> createConversation(@RequestBody ConversationRequestDto conversationRequestDto) {
        conversationService.createConversation(conversationRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponseDto>> getAllConversations() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversationService.getAllConversations());
    }

    // TODO: delete conversation
    // todo: conversation controller handler

    @PostMapping("/{username}")
    public ResponseEntity<Void> sendMessage(@PathVariable("username")
                                            @RequestBody MessageRequestDto messageRequestDto) {
        conversationService.sendMessage(messageRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
