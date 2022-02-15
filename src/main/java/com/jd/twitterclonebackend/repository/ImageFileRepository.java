package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.domain.ImageFileEntity;
import com.jd.twitterclonebackend.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

    ImageFileEntity getByPost(PostEntity post);

    void deleteByPostId(Long postId);

}
