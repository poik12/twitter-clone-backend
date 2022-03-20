package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.NotificationResponseDto;
import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.NotificationType;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationService {

    void notifyFollower(FollowerEntity followerEntity, NotificationType notificationType, Long materialId);

    void notifyUser(UserEntity publisherEntity, NotificationType notificationType, Long materialId);

    List<NotificationResponseDto> getAllNotifications(Pageable pageable);

    void deleteNotificationById(Long notificationId);
}
