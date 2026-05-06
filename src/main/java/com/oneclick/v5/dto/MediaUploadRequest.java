package com.oneclick.v5.dto;

import com.oneclick.v5.validation.FileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record MediaUploadRequest(

    @NotBlank(message = "Issue details is required")
    @Size(min = 10, message = "Issue details must be at least 10 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9.,\\s]+$", 
             message = "Issue details can only contain alphanumeric characters, spaces, full stop (.) and comma (,)")
    String issueDetails,

    @NotNull(message = "File is required")
    @FileSize(max = "10MB")   // 10MB is safe upper bound; we enforce 2MB for images in service
    MultipartFile file
) {}
