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

    @Column(length = 280)
    private String description;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private VerificationTokenEntity verificationTokenEntity;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private RefreshTokenEntity refreshTokenEntity;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="to"
    ) // user has many followers
    private List<FollowerEntity> followers;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy="from"
    ) // user has many following
    private List<FollowerEntity> following;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    ) // user has many posts
    private List<PostEntity> posts;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    ) // user has many comments
    private List<CommentEntity> comments;

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
