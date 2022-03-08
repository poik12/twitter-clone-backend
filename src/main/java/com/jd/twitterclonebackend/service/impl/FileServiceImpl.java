package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.repository.ImageFileRepository;
import com.jd.twitterclonebackend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
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
    public void uploadImageFile(PostEntity post, MultipartFile file) {

        try {
            System.out.println("Original image byte size - " + file.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        ImageFileEntity imageFileEntity = new ImageFileEntity();

        try {
            imageFileEntity.setName(fileName);
            imageFileEntity.setContent(compressBytes(file.getBytes()));
            imageFileEntity.setSize(file.getSize());
            imageFileEntity.setPost(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageFileRepository.save(imageFileEntity);
    }

    // Compress the image bytes before storing it in database
    private byte[] compressBytes(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(
                    buffer,
                    0,
                    count
            );

            try {
                outputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        System.out.println("Compressed image byte size - " + outputStream.toByteArray().length);

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

        Map<Long, byte[]> imageFileMap = new HashMap<>();

        for (PostEntity postEntity : postList) {

            ImageFileEntity imageFileEntity = imageFileRepository.getByPost(postEntity);

            if (Objects.isNull(imageFileEntity)) {
                continue;
            } else {
                imageFileMap.put(
                        imageFileEntity.getPost().getId(),
                        decompressBytes(imageFileEntity.getContent())
                );
            }
        }
        return imageFileMap;
    }

    // Uncompress retrieved image bytes from database
    public byte[] decompressBytes(byte[] data) {

        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(
                        buffer,
                        0,
                        count
                );
                outputStream.close();
            }
        } catch (DataFormatException | IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    // CONVERT IMAGE TO BYTE ARRAY USING IMAGE PATH
    @Override
    @Nullable
    public byte[] convertImagePathToByteArray(String imagePath) {
        byte[] pictureBytes = null;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            pictureBytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pictureBytes;
    }

    // CONVERT IMAGE TO BYTE ARRAY USING IMAGE FILE
    @Override
    public byte[] convertImageFileToByteArray(MultipartFile imageFile) {
        try {
            return imageFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
