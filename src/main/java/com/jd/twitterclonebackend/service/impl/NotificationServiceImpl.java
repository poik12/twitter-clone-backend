package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.NotificationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.mapper.NotificationMapper;
import com.jd.twitterclonebackend.repository.NotificationRepository;
import com.jd.twitterclonebackend.service.NotificationResponseDto;
import com.jd.twitterclonebackend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    private final UserDetailsServiceImpl userDetailsService;

    private final NotificationRepository notificationRepository;

    @Override
    public void notifyFollower(FollowerEntity followerEntity,
                               NotificationType notificationType,
                               Long materialId) {
        NotificationEntity notificationEntity = notificationMapper.mapFromDtoToEntity(
                followerEntity,
                notificationType,
                materialId
        );
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void notifyUser(UserEntity tweetPublisher,
                           NotificationType notificationType,
                           Long materialId) {
        UserEntity loggedUser = userDetailsService.currentLoggedUserEntity();

        if (Objects.equals(tweetPublisher.getId(), loggedUser.getId())) {
            return;
        }
        NotificationEntity notificationEntity = notificationMapper.mapFromDtoToEntity(
                loggedUser,
                tweetPublisher,
                notificationType,
                materialId
        );
        notificationRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationResponseDto> getAllNotifications(Pageable pageable) {

        UserEntity loggedUser = userDetailsService.currentLoggedUserEntity();

        return notificationRepository.findBySubscriber(loggedUser, pageable)
                .stream()
                .map(notificationMapper::mapFromEntityToDto)
                .toList();
    }

    @Override
    public void deleteNotificationById(Long notificationId) {
//        notificationRepository
//                .deleteById(notificationId)
//                .orElseThrow(() -> new NotificationException(
//                        InvalidNotificationEnum.NOTIFICATION_NOT_FOUND_WITH_ID.getMessage() + notificationId,
//                        HttpStatus.NOT_FOUND
//                ));
        //todo: implement delete notification by id from current logged user
    }


}
