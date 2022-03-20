package com.jd.twitterclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private String name;
    private String username;
    private byte[] userProfilePicture;
    private String timeDuration;
    private String type;
    private Long materialId;
}
