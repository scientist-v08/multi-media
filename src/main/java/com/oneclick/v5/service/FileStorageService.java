package com.oneclick.v5.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class FileStorageService {

    @Value("${app.media.storage.video-dir:E:/temp/video-conversion}")
    private String videoStorageDir;

    @Value("${app.media.storage.image-dir:E:/temp/picture-conversion}")
    private String imageStorageDir;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS").withZone(ZoneId.systemDefault());

    public String saveConvertedFile(byte[] fileBytes, String originalExtension) throws IOException {
        String mediaType = originalExtension.equals("webm") ? "video" : "image";
        String storagePath = mediaType.equals("video") ? videoStorageDir : imageStorageDir;

        // Create directory if it doesn't exist
        Path dirPath = Paths.get(storagePath);
        Files.createDirectories(dirPath);

        // Generate unique timestamp-based filename
        String timestamp = FORMATTER.format(Instant.now());
        String fileName = timestamp + "." + (originalExtension.equals("webm") ? "mp4" : "jpeg");

        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, fileBytes);

        return fileName;   // this is what we store in DB
    }
}
