package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT m FROM MessageEntity m " +
            "WHERE m.conversation.id = :conversationId " +
            "ORDER BY m.createdAt DESC")
    List<MessageEntity> findMessagesByConversationId(@Param("conversationId") Long conversationId,
                                                     Pageable pageable);

}
