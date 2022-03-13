package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    void deleteByPostId(Long postId);

    @Query(value = "FROM CommentEntity WHERE post = :postEntity ORDER BY createdAt DESC")
    List<CommentEntity> findAllByPostAndOrderByCreatedAtDesc(@Param("postEntity") PostEntity postEntity,
                                                             Pageable pageable);

    @Query(value = "FROM CommentEntity WHERE user = :userEntity ORDER BY createdAt DESC")
    List<CommentEntity> findAllByUserAndOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity,
                                                             Pageable pageable);

    void deleteAllByUser(UserEntity userEntity);
}
