package com.oneclick.v5.entity;

import com.oneclick.v5.constants.MediaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media_issue",
       indexes = {
           @Index(name = "idx_media_type", columnList = "media_type")  // full index as fallback
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;   // <-- single column, clean!

    @Column(nullable = false, length = 500)
    private String picOrVidName;   // filename only (path configured in properties)

    @Column(columnDefinition = "TEXT")
    private String issueDetails;
}
