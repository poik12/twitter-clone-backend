package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

    ImageFileEntity getByPost(PostEntity post);

    @Query(value = "SELECT f FROM ImageFileEntity f WHERE f.post.id = :postId")
    List<ImageFileEntity> findAllByPostId(Long postId);

    void deleteByPostId(Long postId);

}
