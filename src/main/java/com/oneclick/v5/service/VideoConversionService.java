package com.oneclick.v5.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

@Service
public class VideoConversionService {
    public byte[] convertWebmToMp4(MultipartFile webmFile) throws Exception {
        Path inputPath = null;
        Path outputPath = null;

        try {
            inputPath = Files.createTempFile("input_", ".webm");
            outputPath = Files.createTempFile("output_", ".mp4");

            // Save uploaded file
            Files.copy(webmFile.getInputStream(), inputPath, StandardCopyOption.REPLACE_EXISTING);

            File inputFile = inputPath.toFile();
            File outputFile = outputPath.toFile();

            // =============== JAVE2 Encoding Options ===============
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(192000);     // 192 kbps

            VideoAttributes video = new VideoAttributes();
            video.setCodec("libx264");
            video.setBitRate(2000000);    // 2 Mbps
            video.setFrameRate(30);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, attrs);

            return Files.readAllBytes(outputPath);

        } finally {
            // Cleanup
            if (inputPath != null) Files.deleteIfExists(inputPath);
            if (outputPath != null) Files.deleteIfExists(outputPath);
        }
    }
}
