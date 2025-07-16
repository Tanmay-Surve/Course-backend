package com.tanmay.courseapp.dto;

import com.tanmay.courseapp.document.CourseDocument;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDTO {
    private long total;
    private List<CourseDocument> courses;
}