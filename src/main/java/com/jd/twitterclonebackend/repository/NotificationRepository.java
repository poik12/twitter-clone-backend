package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.NotificationEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query(value = "SELECT n from NotificationEntity n " +
            "WHERE n.subscriber = :subscriber " +
            "ORDER BY n.createdAt DESC")
    List<NotificationEntity> findBySubscriber(@Param("subscriber") UserEntity subscriber, Pageable pageable);

    void deleteByMaterialId(Long tweetId);
}
