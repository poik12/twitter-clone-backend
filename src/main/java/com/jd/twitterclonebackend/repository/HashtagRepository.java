package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {

    Optional<HashtagEntity> findByValue(String hashtag);

}
