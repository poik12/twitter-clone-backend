package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.EmailNotificationDto;
import com.jd.twitterclonebackend.entity.UserEntity;

public interface MailService {

    EmailNotificationDto createActivationEmail(UserEntity userEntity, String verificationToken);

    String buildEmailBody(String name, String link);

    void sendEmail(EmailNotificationDto emailNotificationDto);

}
