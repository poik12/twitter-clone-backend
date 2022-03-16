package com.jd.twitterclonebackend.entity;

import com.jd.twitterclonebackend.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "publisher_id",
            referencedColumnName = "id"
    ) // user can have many notifications
    private UserEntity publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subscriber_id",
            referencedColumnName = "id"
    )
    private UserEntity subscriber;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long materialId;

    @CreationTimestamp
    private Date  createdAt;

}
