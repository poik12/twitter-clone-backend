package com.jd.twitterclonebackend.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "followers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="to_user_fk",
            referencedColumnName = "id"
    )
    private UserEntity to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="from_user_fk",
            referencedColumnName = "id"
    )
    private UserEntity from;

    public FollowerEntity(UserEntity to, UserEntity from) {
        this.to = to;
        this.from = from;
    }
}
