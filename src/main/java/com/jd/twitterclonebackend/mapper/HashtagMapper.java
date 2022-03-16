package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import org.springframework.stereotype.Component;

@Component
public class HashtagMapper {

    public HashtagEntity mapFromDtoToEntity(String hashtag) {

        return HashtagEntity.builder()
                .value(hashtag)
                .build();

    }
}
