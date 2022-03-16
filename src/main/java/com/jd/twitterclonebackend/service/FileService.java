package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.entity.TweetEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    // Upload image file into db
    void uploadImageFile(TweetEntity tweetEntity, MultipartFile file);
    // Get image file list fro post
    TweetResponseDto getAllImageFilesForPost(TweetResponseDto tweetResponseDtoList);
    // Get array from image path
    byte[] convertFilePathToByteArray(String imagePath);
    // Get array from multipart file
    byte[] convertFileToByteArray(MultipartFile imageFile);
}
