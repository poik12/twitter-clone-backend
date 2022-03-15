package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.request.ConversationRequestDto;
import com.jd.twitterclonebackend.dto.request.MessageRequestDto;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import com.jd.twitterclonebackend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/conversations")
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
    public ResponseEntity<List<ConversationResponseDto>> getAllConversations(@RequestParam("pageNumber") int pageNumber,
                                                                             @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversationService.getAllConversations(pageable));
    }

    // TODO: delete conversation
    // todo: conversation controller handler


    @GetMapping(path = "/{conversationId}")
    public ResponseEntity<ConversationResponseDto> getConversationById(
            @PathVariable("conversationId") Long conversationId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversationService.getConversationById(conversationId));
    }

    @PostMapping(path = "/messages")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageRequestDto messageRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conversationService.sendMessage(messageRequestDto));
    }

    @GetMapping(path = "/messages/{conversationId}")
    ResponseEntity<List<MessageResponseDto>> getMessagesForConversationById(@PathVariable Long conversationId,
                                                                            @RequestParam("pageNumber") int pageNumber,
                                                                            @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversationService.getMessagesForConversationById(conversationId, pageable));
    }

}