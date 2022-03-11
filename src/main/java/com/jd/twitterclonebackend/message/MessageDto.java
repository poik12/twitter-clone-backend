package com.jd.twitterclonebackend.message;

import com.jd.twitterclonebackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private UserEntity to;
    private UserEntity from;
    private String content;
//    private Instant createdAt;

}
