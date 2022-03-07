package com.jd.twitterclonebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class EmailNotificationDto {

    String emailSubject;
    String emailRecipient;
    String recipientName;
    String activationLink;

}