package com.jd.twitterclonebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private UserEntity user;

    @CreationTimestamp
    private Date createdAt;

    private Instant expiresAt;

    public RefreshTokenEntity(String token, Instant expiresAt, UserEntity user) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}