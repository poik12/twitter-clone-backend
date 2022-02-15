package com.jd.twitterclonebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
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

    @Lob
    private String description;

    private String url;

    @CreationTimestamp
    private Instant createdAt;

    private Long commentNo;

    // Many posts created by one user
    @ManyToOne(fetch = FetchType.LAZY) // some error during getting all post from db
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments = new ArrayList<>();


    // One post can have multiple images
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private List<ImageFileEntity> images = new ArrayList<>();

}
