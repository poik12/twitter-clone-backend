package com.jd.twitterclonebackend.entity;

import com.jd.twitterclonebackend.dto.response.MessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="creator",
            referencedColumnName = "id"
    )
    private UserEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="participant",
            referencedColumnName = "id"
    )
    private UserEntity participant;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "conversation"
    ) // one conversation has many massages
    private List<MessageEntity> messages;

    private Date latestMessageTime;

    private String latestMessageContent;

    private Boolean latestMessageRead;

    @CreationTimestamp
    private Date createdAt;
}
