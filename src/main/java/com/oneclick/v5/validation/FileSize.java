package com.oneclick.v5.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface FileSize {
    String message() default "File size must not exceed {max}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String max() default "10MB";
}
