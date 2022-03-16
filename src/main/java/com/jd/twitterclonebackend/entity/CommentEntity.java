package com.jd.twitterclonebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 280)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tweet_id",
            referencedColumnName = "id"
    )
    private TweetEntity tweet;

    @CreationTimestamp
    private Instant createdAt;

}
