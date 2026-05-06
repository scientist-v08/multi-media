package com.oneclick.v5.controller;

import com.oneclick.v5.dto.MediaUploadRequest;
import com.oneclick.v5.dto.MediaUploadResponse;
import com.oneclick.v5.entity.MediaIssue;
import com.oneclick.v5.service.MediaService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
@Validated
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaUploadResponse> uploadMedia(
            @Valid @ModelAttribute MediaUploadRequest request) throws Exception {

        MediaUploadResponse response = mediaService.uploadMedia(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<MediaIssue> getAllMedia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return mediaService.getAllMedia(pageable);
    }

    @GetMapping("/pictures")
    public Page<MediaIssue> getPictures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return mediaService.getMediaByType(com.oneclick.v5.constants.MediaType.PIC, pageable);
    }

    @GetMapping("/videos")
    public Page<MediaIssue> getVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return mediaService.getMediaByType(com.oneclick.v5.constants.MediaType.VIDEO, pageable);
    }
}