package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.repository.ImageFileRepository;
import com.jd.twitterclonebackend.service.FileService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final ImageFileRepository imageFileRepository;

    // Upload image into database
    @Override
    public void uploadImageFile(TweetEntity tweetEntity, MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        byte[] content = convertFileToByteArray(file);

        ImageFileEntity imageFileEntity = ImageFileEntity.builder()
                .name(fileName)
                .content(compressBytes(content))
                .size(file.getSize())
                .tweet(tweetEntity)
                .build();

        imageFileRepository.save(imageFileEntity);
    }

    // Compress the image bytes before storing it in database
    private byte[] compressBytes(byte[] content) {

        Deflater deflater = new Deflater();
        deflater.setInput(content);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
        byte[] buffer = new byte[1024];

        log.info("Original image byte size: " + content.length);
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Compressed image byte size: " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // Get all images for post form database
    public TweetResponseDto getAllImageFilesForPost(TweetResponseDto tweetResponseDto) {

        // Get content list for post
        List<byte[]> contentList = imageFileRepository
                .findAllByPostId(tweetResponseDto.getId())
                .stream()
                .map(imageFileEntity -> decompressBytes(imageFileEntity.getContent()))
                .toList();
        if (!contentList.isEmpty()) {
            tweetResponseDto.setFileContent(contentList);
        }
        return tweetResponseDto;
    }

    // Uncompress retrieved image bytes from database
    public byte[] decompressBytes(byte[] content) {

        Inflater inflater = new Inflater();
        inflater.setInput(content);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
        byte[] buffer = new byte[1024];

        log.info("Compressed image byte size: " + outputStream.toByteArray().length);
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
                outputStream.close();
            }
        } catch (DataFormatException | IOException e) {
            e.printStackTrace();
        }
        log.info("Decompressed image byte size: " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // Convert image to byte array using image path
    @Override
    public byte[] convertFilePathToByteArray(String filePath) {
        byte[] content = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            content = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    // Convert file to byte array using image file
    @Override
    public byte[] convertFileToByteArray(MultipartFile file) {
        byte[] content = null;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
