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
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity implements Serializable {

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

//    @OneToMany(mappedBy = "post")
//    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "post"
    ) // many comments belong to post
    private List<CommentEntity> comments;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "post"
    )
    private List<ImageFileEntity> images;

    private Long commentNo;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // One post can have multiple images
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private List<ImageFileEntity> images = new ArrayList<>();

}