package com.jd.twitterclonebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
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

    private String name;

    private String username;

    private String emailAddress;

    private String password;

    private String phoneNumber;

    private Instant createdAt;

    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Lob
    private byte[] userProfilePicture;

    @Lob
    private byte[] userBackgroundPicture;

    private Long tweetNo;

    private Long followerNo;

    private Long followingNo;

    @Lob
    private String description;

    @OneToMany(mappedBy="to")
    private List<FollowerEntity> followers;

    @OneToMany(mappedBy="from")
    private List<FollowerEntity> following;

}
