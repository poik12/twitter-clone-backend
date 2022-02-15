package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.domain.CommentEntity;
import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // TODO: Doesn't work?
    List<CommentEntity> findAllByPost(PostEntity post);

    List<CommentEntity> findAllByUser(UserEntity userEntity);

    void deleteByPostId(Long postId);
}
