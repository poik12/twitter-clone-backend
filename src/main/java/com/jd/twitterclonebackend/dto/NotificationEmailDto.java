package com.jd.twitterclonebackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationEmailDto {

    private String emailSubject;

    private String emailRecipient;

    private String recipientName;

    private String activationLink;

}