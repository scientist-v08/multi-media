package com.oneclick.v5.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oneclick.v5.constants.MediaType;
import com.oneclick.v5.entity.MediaIssue;

@Repository
public interface MediaIssueRepository extends JpaRepository<MediaIssue, Long> {
    // Pageable + filter
    Page<MediaIssue> findByMediaType(MediaType mediaType, Pageable pageable);
    // Get ALL media issues with pagination
    Page<MediaIssue> findAll(Pageable pageable);
}
