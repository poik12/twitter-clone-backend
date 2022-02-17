package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.NotificationEmailDto;
import com.jd.twitterclonebackend.domain.UserEntity;

public interface MailService {

    NotificationEmailDto createActivationEmailForUser(UserEntity userEntity, String verificationToken);

    String buildEmailBodyForUser(String name, String link);

    void sendEmailToUser(NotificationEmailDto notificationEmailDto);

}
