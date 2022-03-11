package com.jd.twitterclonebackend.dto.response;

import com.jd.twitterclonebackend.entity.UserEntity;
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
    private Long senderId;
    private Long recipientId;
    private String createdAt;


}
