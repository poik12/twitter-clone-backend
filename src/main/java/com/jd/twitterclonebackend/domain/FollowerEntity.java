package com.jd.twitterclonebackend.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "followers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="to_user_fk")
    private UserEntity to;

    @ManyToOne
    @JoinColumn(name="from_user_fk")
    private UserEntity from;

    public FollowerEntity(UserEntity to, UserEntity from) {
        this.to = to;
        this.from = from;
    }
}
