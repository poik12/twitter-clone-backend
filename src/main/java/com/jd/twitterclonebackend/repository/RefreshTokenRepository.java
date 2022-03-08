package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.RefreshTokenEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByToken(String token);

    void deleteAllByUser(UserEntity userEntity);

    RefreshTokenEntity getRefreshTokenByUser(UserEntity userEntity);
}
