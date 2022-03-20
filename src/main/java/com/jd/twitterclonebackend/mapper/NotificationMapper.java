package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.NotificationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.dto.response.NotificationResponseDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
public class NotificationMapper {

    public NotificationEntity mapFromDtoToEntity(FollowerEntity followerEntity,
                                                 NotificationType notificationType,
                                                 Long materialId) {

        if (Objects.isNull(followerEntity)
                || Objects.isNull(notificationType)) {
            return null;
        }

        return NotificationEntity.builder()
                .publisher(followerEntity.getTo())
                .subscriber(followerEntity.getFrom())
                .notificationType(notificationType)
                .materialId(materialId)
                .build();
    }

    public NotificationEntity mapFromDtoToEntity(UserEntity loggedUser,
                                                 UserEntity tweetPublisher,
                                                 NotificationType notificationType,
                                                 Long materialId) {

        if (Objects.isNull(loggedUser)
                || Objects.isNull(tweetPublisher)
                || Objects.isNull(notificationType)) {
            return null;
        }

        return NotificationEntity.builder()
                .publisher(loggedUser)
                .subscriber(tweetPublisher)
                .notificationType(notificationType)
                .materialId(materialId)
                .build();
    }

    public NotificationResponseDto mapFromEntityToDto(NotificationEntity notificationEntity) {

        if (Objects.isNull(notificationEntity)) {
            return null;
        }

        return NotificationResponseDto.builder()
                .id(notificationEntity.getId())
                .name(notificationEntity.getPublisher().getName())
                .username(notificationEntity.getPublisher().getUsername())
                .userProfilePicture(notificationEntity.getPublisher().getProfilePicture())
                .timeDuration(TimeAgo.using(Instant.now().toEpochMilli()))
                .type(notificationEntity.getNotificationType().getType())
                .materialId(notificationEntity.getMaterialId())
                .build();
    }


}
