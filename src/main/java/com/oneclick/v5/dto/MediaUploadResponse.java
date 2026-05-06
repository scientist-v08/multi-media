package com.oneclick.v5.dto;

public record MediaUploadResponse(
    Long id,
    String mediaType,
    String fileName,
    String issueDetails,
    String message
) {}
