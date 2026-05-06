package com.oneclick.v5.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private long maxBytes;

    private static final Pattern SIZE_PATTERN = Pattern.compile("^(\\d+)([BKMG]B?)?$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxBytes = parseSize(constraintAnnotation.max());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // @NotNull will catch this
        }
        return file.getSize() <= maxBytes;
    }

    private long parseSize(String sizeStr) {
        Matcher matcher = SIZE_PATTERN.matcher(sizeStr.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid size format: " + sizeStr);
        }
        long value = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2) != null ? matcher.group(2).toUpperCase() : "B";

        return switch (unit) {
            case "KB", "K" -> value * 1024;
            case "MB", "M" -> value * 1024 * 1024;
            case "GB", "G" -> value * 1024 * 1024 * 1024;
            default -> value;
        };
    }
}
