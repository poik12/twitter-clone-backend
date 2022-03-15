package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {
    // Upload image file into db
    void uploadImageFile(PostEntity postEntity, MultipartFile file);
    // Get image file list fro post
    PostResponseDto getAllImageFilesForPost(PostResponseDto postResponseDtoList);
    // Get array from image path
    byte[] convertFilePathToByteArray(String imagePath);
    // Get array from multipart file
    byte[] convertFileToByteArray(MultipartFile imageFile);
}
