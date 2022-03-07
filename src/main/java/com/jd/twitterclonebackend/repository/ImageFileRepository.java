package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

    ImageFileEntity getByPost(PostEntity post);

    void deleteByPostId(Long postId);

}
