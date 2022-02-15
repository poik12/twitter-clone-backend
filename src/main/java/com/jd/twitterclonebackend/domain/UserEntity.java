package com.jd.twitterclonebackend.domain;

import com.jd.twitterclonebackend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
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

    private String name;

    private String username;

    private String emailAddress;

    private String password;

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

    @Lob
    private String description;

    @OneToMany(mappedBy="to")
    private List<FollowerEntity> followers;

    @OneToMany(mappedBy="from")
    private List<FollowerEntity> following;

}
