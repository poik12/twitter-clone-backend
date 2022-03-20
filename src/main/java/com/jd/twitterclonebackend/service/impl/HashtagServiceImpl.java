package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.mapper.HashtagMapper;
import com.jd.twitterclonebackend.repository.HashtagRepository;
import com.jd.twitterclonebackend.service.HashtagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final HashtagMapper hashtagMapper;

    @Override
    public List<HashtagEntity> checkHashTags(String input) {

        List<String> hashTagList = validateHashtags(input);

        if (hashTagList.size() == 0) {
            return Collections.emptyList();
        }

        List<HashtagEntity> hashTagListFromDB = new ArrayList<>();

        List<String> hashTagsListNotPresentInDb = hashTagList.stream()
                .filter(hashtag -> {
                    Optional<HashtagEntity> hashtagEntityOptional = hashtagRepository.findByValue(hashtag);
                    if (hashtagEntityOptional.isPresent()) {
                        hashTagListFromDB.add(hashtagEntityOptional.get());
                        return false;
                    }
                    return true;
                })
                .toList();

        if (hashTagsListNotPresentInDb.size() == 0) {
            return hashTagListFromDB;
        }

        List<HashtagEntity> newHashtagEntityList = hashTagsListNotPresentInDb.stream()
                    .map(hashtagMapper::mapFromDtoToEntity)
                    .map(hashtagRepository::save)
                    .collect(Collectors.toList());

        newHashtagEntityList.addAll(hashTagListFromDB);
        return newHashtagEntityList;
    }

    private List<String> validateHashtags(String input) {
        List<String> hashTagList = new ArrayList<>();

        Matcher matcher = Pattern.compile(
                "(#\\w+)\\b",
                Pattern.CASE_INSENSITIVE
        ).matcher(input);

        while (matcher.find()) {
            hashTagList.add(matcher.group());
        }
        return hashTagList;
    }
}
