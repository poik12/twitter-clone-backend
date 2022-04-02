package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.ConversationEntity;
import com.jd.twitterclonebackend.entity.MessageEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {

    @Query(value = "SELECT m from ConversationEntity m " +
            "WHERE m.creator = :creator OR m.participant = :participant " +
            "ORDER BY m.latestMessageContent DESC")
    List<ConversationEntity> findAllByCreatorOrParticipant(UserEntity creator,
                                                           UserEntity participant,
                                                           Pageable pageable);

    @Modifying
    @Query(value = "UPDATE ConversationEntity c " +
            "SET c.latestMessageContent = :messageContent, c.latestMessageTime = :messageCreatedAt " +
            "WHERE c.id = :conversationId")
    void updateLatestMessageContentAndTime(@Param("conversationId") Long conversationId,
                                           @Param("messageContent") String messageContent,
                                           @Param("messageCreatedAt") Date messageCreatedAt);
}
