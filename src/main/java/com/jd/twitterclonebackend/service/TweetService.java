package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedTweetResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TweetService {

    void addTweet(MultipartFile[] file, String tweetRequestJson);

    List<TweetResponseDto> getAllTweets(Pageable pageable);

    TweetResponseDto getTweetById(Long tweetId);

    List<TweetResponseDto> getTweetsByUsername(String username, Pageable pageable);

    void deleteTweetById(Long tweetId);

    void likeTweetById(Long tweetId);

    List<TweetResponseDto> getLikedTweetsByUsername(String username, Pageable pageable);

    List<RepliedTweetResponseDto> getRepliedTweetsWithCommentsByUsername(String username, Pageable pageable);
}
