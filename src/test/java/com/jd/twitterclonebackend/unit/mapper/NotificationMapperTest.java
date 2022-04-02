package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.entity.NotificationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.mapper.NotificationMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationMapperTest extends UnitTestInitData {

    @InjectMocks
    private NotificationMapper underTest;

    @Test
    @DisplayName(value = "Should map from Notification Dto to Notification Entity")
    void should_mapFromNotificationDto_toNotificationEntity() {
        // given
        UserEntity primeUser = initPrimeUser();
        UserEntity secondUser = initSecondUser();

        // when
        var result = underTest.mapFromDtoToEntity(
                primeUser,
                secondUser,
                NotificationType.TWEET,
                1L
        );

        // then
        assertAll(
                () -> {
                    assertEquals(primeUser, result.getPublisher());
                    assertEquals(secondUser, result.getSubscriber());
                    assertEquals(NotificationType.TWEET, result.getNotificationType());
                    assertEquals(1L, result.getMaterialId());
                }
        );
    }

    @Test
    @DisplayName(value = "Should map from Notification Entity to Notification Dto")
    void should_mapFromNotificationEntity_toNotificationDto() {
        // given
        NotificationEntity notificationEntity = initNotificationEntity();

        // when
        var result = underTest.mapFromEntityToDto(notificationEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(notificationEntity.getId(), result.getId());
                    assertEquals(notificationEntity.getPublisher().getName(), result.getName());
                    assertEquals(notificationEntity.getPublisher().getUsername(), result.getUsername());
                    assertEquals(notificationEntity.getPublisher().getProfilePicture(),
                            result.getUserProfilePicture());
                    assertEquals(notificationEntity.getNotificationType().getType(), result.getType());
                    assertEquals(notificationEntity.getMaterialId(), result.getMaterialId());
                }
        );
    }

}