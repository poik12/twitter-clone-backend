package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.entity.HashtagEntity;

import java.util.List;

public interface HashtagService {

    List<HashtagEntity> checkHashTags(String string);

}
