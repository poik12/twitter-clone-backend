package com.jd.twitterclonebackend.entity;

import com.jd.twitterclonebackend.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Email
    private String emailAddress;

    @Column(nullable = false)
    private String phoneNumber;

    @Lob
    private byte[] profilePicture;

    @Lob
    private byte[] backgroundPicture;

    private Long tweetNo;

    private Long followerNo;

    private Long followingNo;

    @Column(length = 1000)
    private String description;

    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private VerificationTokenEntity verificationTokenEntity;

    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private RefreshTokenEntity refreshTokenEntity;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy="to"
    ) // user has many followers
    private List<FollowerEntity> followers;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy="from"
    ) // user has many following
    private List<FollowerEntity> following;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    ) // user has many posts
    private List<PostEntity> posts;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    ) // user has many comments
    private List<CommentEntity> comments;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="creator"
    ) // user created conversations with other users
    private List<ConversationEntity> conversationCreator;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="participant"
    ) // user was added to conversation by other user
    private List<ConversationEntity> conversationParticipant;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="sender"
    ) // user sent many messages to other users
    private List<MessageEntity> messagesSent;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="recipient"
    ) // user received many massages from other users
    private List<MessageEntity> messagesReceived;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_post_likes",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "id"
            )
    ) // many users like many posts
    private List<PostEntity> likedPosts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean enabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Date updatedAt;
}
