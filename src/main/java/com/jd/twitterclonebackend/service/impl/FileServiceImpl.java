package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.repository.ImageFileRepository;
import com.jd.twitterclonebackend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final ImageFileRepository imageFileRepository;

    // UPLOAD IMAGE INTO DATABASE
    @Override
    public void uploadImageFile(PostEntity postEntity, MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        byte[] content = convertFileToByteArray(file);

        ImageFileEntity imageFileEntity = ImageFileEntity.builder()
                .name(fileName)
                .content(compressBytes(content))
                .size(file.getSize())
                .post(postEntity)
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

    // GET ALL IMAGES FROM DATABASE
    @Override
    public Map<Long, byte[]> getAllImageFiles() {
        // Get content from image entities
        return imageFileRepository
                .findAll()
                .stream()
                .collect(Collectors.toMap(
                        imageFileEntity -> imageFileEntity.getPost().getId(),
                        imageFileEntity -> decompressBytes(imageFileEntity.getContent()
                        ))
                );
    }

    // GET IMAGE FILE MAP BY POST LIST
    @Override
    public Map<Long, byte[]> getImageFilesByPostList(List<PostEntity> postList) {
        return postList.stream()
                .map(imageFileRepository::getByPost)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        fileEntity -> fileEntity.getPost().getId(),
                        fileEntity -> decompressBytes(fileEntity.getContent())
                ));
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

    // CONVERT IMAGE TO BYTE ARRAY USING IMAGE PATH
    @Override
    @Nullable
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

    // CONVERT FILE TO BYTE ARRAY USING IMAGE FILE
    @Override
    @Nullable
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
