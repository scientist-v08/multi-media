package com.oneclick.v5.service;

import com.oneclick.v5.constants.MediaType;
import com.oneclick.v5.dto.MediaUploadRequest;
import com.oneclick.v5.dto.MediaUploadResponse;
import com.oneclick.v5.entity.MediaIssue;
import com.oneclick.v5.repository.MediaIssueRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class MediaService {

    private final MediaIssueRepository repository;
    private final MediaConversionService conversionService;
    private final FileStorageService storageService;

    public MediaService(MediaIssueRepository repository,
                        MediaConversionService conversionService,
                        FileStorageService storageService) {
        this.repository = repository;
        this.conversionService = conversionService;
        this.storageService = storageService;
    }

    public MediaUploadResponse uploadMedia(MediaUploadRequest request) throws Exception {
        MultipartFile file = request.file();

        // Additional validation (file type + size for images)
        validateFile(file);

        // Convert
        byte[] convertedBytes = conversionService.convert(file);

        // Determine media type
        String originalName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        MediaType mediaType = originalName.endsWith(".webm") ? MediaType.VIDEO : MediaType.PIC;

        // Save converted file to disk
        String storedFileName = storageService.saveConvertedFile(
                convertedBytes,
                originalName.endsWith(".webm") ? "webm" : "png"
        );

        // Save to database
        MediaIssue entity = new MediaIssue();
        entity.setMediaType(mediaType);
        entity.setPicOrVidName(storedFileName);
        entity.setIssueDetails(request.issueDetails());

        MediaIssue saved = repository.save(entity);

        return new MediaUploadResponse(
                saved.getId(),
                mediaType.name(),
                storedFileName,
                saved.getIssueDetails(),
                "Media uploaded and converted successfully"
        );
    }

    private void validateFile(MultipartFile file) {
        String filename = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();

        if (filename.endsWith(".webm")) {
            // already validated by @FileSize(10MB)
        } else if (filename.endsWith(".png")) {
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("Image file must not exceed 2MB");
            }
        } else {
            throw new IllegalArgumentException("Only .webm video or .png image files are allowed");
        }
    }

    public Page<MediaIssue> getAllMedia(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<MediaIssue> getMediaByType(MediaType mediaType, Pageable pageable) {
        return repository.findByMediaType(mediaType, pageable);
    }
}
