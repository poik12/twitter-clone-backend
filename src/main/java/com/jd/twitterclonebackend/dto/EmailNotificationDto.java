package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationDto {

    private String emailSubject;
    private String emailRecipient;
    private String recipientName;
    private String activationLink;

}