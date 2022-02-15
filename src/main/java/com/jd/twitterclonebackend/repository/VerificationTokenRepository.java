package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.domain.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

    Optional<VerificationTokenEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE VerificationTokenEntity c " +
            "SET c.confirmedAt = :confirmedAt " +
            "WHERE c.token = :token")
    void updateConfirmedAt(String token, Instant confirmedAt);

    void deleteAllByUser(UserEntity userEntity);
}
