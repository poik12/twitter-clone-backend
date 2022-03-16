package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedTweetResponseDto;
import com.jd.twitterclonebackend.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/tweets")
@RequiredArgsConstructor
@ApiRestController
public class TweetController {

    private final TweetService tweetService;

    // ADD NEW POST
    @PostMapping
    public ResponseEntity<Void> addTweet(
            @RequestParam(required = false, value = "files") MultipartFile[] files,
            @RequestParam(required = true, value = "tweetRequestJson") String tweetRequestJson
    ) {
        tweetService.addTweet(files, tweetRequestJson);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    // GET ALL POSTS IN PAGES SORTED BY TIMESTAMP DESC
    @GetMapping()
    public ResponseEntity<List<TweetResponseDto>> getAllTweets(@RequestParam("pageNumber") int pageNumber,
                                                               @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tweetService.getAllTweets(pageable));
    }

    // GET SINGLE POST BY ID
    @GetMapping(path = "/{tweetId}")
    public ResponseEntity<TweetResponseDto> getTweetById(@PathVariable Long tweetId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tweetService.getTweetById(tweetId));
    }

    // GET POSTS BY USERNAME
    @GetMapping(path = "/by-user/{username}")
    public ResponseEntity<List<TweetResponseDto>> getTweetsByUsername(@PathVariable String username,
                                                                      @RequestParam("pageNumber") int pageNumber,
                                                                      @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tweetService.getTweetsByUsername(username, pageable));
    }

    // DELETE POST BY ID
    @DeleteMapping(path = "/{tweetId}")
    public ResponseEntity<Void> deleteTweetById(@PathVariable Long tweetId) {
        tweetService.deleteTweetById(tweetId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/by-tweet/{username}")
    public ResponseEntity<List<RepliedTweetResponseDto>> getRepliedTweetsWithCommentsByUsername(
            @PathVariable String username,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tweetService.getRepliedTweetsWithCommentsByUsername(username, pageable));
    }

    // LIKE POST
    @GetMapping(path = "/like/{tweetId}")
    public ResponseEntity<Void> likeTweet(@PathVariable Long tweetId) {
        tweetService.likeTweetById(tweetId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();

        // todo: send notification to post creator - check who liked which post
    }

    // GET LIKED POSTS
    @GetMapping(path = "/like/by-user/{username}")
    public ResponseEntity<List<TweetResponseDto>> getLikedTweetsByUsername(@PathVariable String username,
                                                                           @RequestParam("pageNumber") int pageNumber,
                                                                           @RequestParam("pageSize") int pageSize) {
        // todo: add column creatAt to user_post_list and and search by it
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tweetService.getLikedTweetsByUsername(username, pageable));
    }

}
