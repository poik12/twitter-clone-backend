package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HashtagMapper {

    public HashtagEntity mapFromDtoToEntity(String hashtag) {
        return HashtagEntity.builder()
                .value(hashtag)
                .build();
    }

    public List<String> mapFromEntityToStringList(List<HashtagEntity> hashtagEntityList) {
        return hashtagEntityList
                .stream()
                .map(HashtagEntity::getValue)
                .collect(Collectors.toList());
    }
}
