package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.domain.PostEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {
    // Upload image file into db
    void uploadImageFile(PostEntity post, MultipartFile file);
    // Get image file from db
    Map<Long, byte[]> getAllImageFiles();
    // Get image file by post list
    Map<Long, byte[]> getImageFilesByPostList(List<PostEntity> postList);
}
