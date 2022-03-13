package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(value = "SELECT p FROM PostEntity p " +
            "WHERE p.user = :userEntity " +
            "ORDER BY p.createdAt DESC")
    List<PostEntity> findAllByUserAndOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity,
                                                          Pageable pageable);

    List<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    PostEntity findByDescription(String description);

    void deleteAllByUser(UserEntity userEntity);

    List<PostEntity> findByUserLikes(UserEntity userEntity, Pageable pageable);

}
