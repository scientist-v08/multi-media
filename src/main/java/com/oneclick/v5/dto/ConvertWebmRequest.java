package com.oneclick.v5.dto;

import org.springframework.web.multipart.MultipartFile;

import com.oneclick.v5.validation.FileSize;

import jakarta.validation.constraints.NotNull;

public record ConvertWebmRequest(
    @NotNull(message = "File is required")
    @FileSize(max = "10MB")
    MultipartFile file
) {}
