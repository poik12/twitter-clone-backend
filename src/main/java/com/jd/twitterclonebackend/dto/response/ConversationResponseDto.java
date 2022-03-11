package com.jd.twitterclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResponseDto {

    private String id;
    private String participantName;
    private String participantUsername;
    private String creatorName;
    private String creatorUsername;
    private String latestMessageTime;
    private String latestMessageContent;
    private Boolean latestMessageRead;
    private List<MessageResponseDto> messages;

}
