package com.jd.twitterclonebackend.domain;

import com.jd.twitterclonebackend.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

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

    @Column(length = 280)
    private String description;

    @OneToMany(mappedBy="to") // relationship owner
    private List<FollowerEntity> followers;

    @OneToMany(mappedBy="from") // relationship owner
    private List<FollowerEntity> following;

}
