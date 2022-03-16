package com.jd.twitterclonebackend.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetRequestDto {

    private String description;

}
