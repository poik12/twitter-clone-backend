package com.jd.twitterclonebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 280)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // Many posts created by one user
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    ) // many posts belong to one user
    private UserEntity user;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "tweet"
    ) // many comments belong to post
    private List<CommentEntity> comments;

    private Long commentNo;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "tweet"
    )
    private List<ImageFileEntity> images;

    @ManyToMany(
            mappedBy = "likedTweets",
            cascade = CascadeType.ALL
    )
    private List<UserEntity> userLikes;

    @ManyToMany
    @JoinTable(
            name = "tweets_hashtags",
            joinColumns = @JoinColumn(
                    name = "tweet_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "hashtag_id",
                    referencedColumnName = "id"
            )
    ) // many posts have many hashtags
    private List<HashtagEntity> hashtags;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
