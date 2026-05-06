package com.oneclick.v5.controller;

import jakarta.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.oneclick.v5.dto.ConvertWebmRequest;
import com.oneclick.v5.service.VideoConversionService;

@RestController
@RequestMapping("/api/video")
@Validated
public class VideoController {
    private final VideoConversionService conversionService;

    public VideoController(VideoConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping(value = "/convert-webm-to-mp4", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> convertWebmToMp4(
        @Valid @ModelAttribute ConvertWebmRequest request
    ) throws Exception {

        byte[] mp4Bytes = conversionService.convertWebmToMp4(request.file());

        ByteArrayResource resource = new ByteArrayResource(mp4Bytes);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.mp4\"")
                .contentLength(mp4Bytes.length)
                .body(resource);
    }
}
