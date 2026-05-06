package com.oneclick.v5.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class MediaConversionService {

    private final VideoConversionService videoConversionService;

    public MediaConversionService(VideoConversionService videoConversionService) {
        this.videoConversionService = videoConversionService;
    }

    public byte[] convert(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename().toLowerCase();

        if (filename.endsWith(".webm")) {
            return videoConversionService.convertWebmToMp4(file);
        } else if (filename.endsWith(".png")) {
            return convertPngToJpeg(file);
        } else {
            throw new IllegalArgumentException("Only .webm or .png files are allowed");
        }
    }

    private byte[] convertPngToJpeg(MultipartFile pngFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(pngFile.getInputStream());

        // Create a new RGB image (removes alpha channel)
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, 0, 0, Color.WHITE, null);
        g.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(newImage, "jpeg", baos);
            return baos.toByteArray();
        }
    }
}
