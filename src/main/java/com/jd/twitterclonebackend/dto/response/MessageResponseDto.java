package com.jd.twitterclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private String content;
    private String senderUsername;
    private String recipientUsername;
    private String createdAt;

}
