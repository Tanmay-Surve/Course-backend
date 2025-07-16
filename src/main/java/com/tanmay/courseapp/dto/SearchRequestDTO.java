package com.tanmay.courseapp.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String query;
    private String category;
    private Integer minAge;
    private Integer maxAge;
    private String type;
    private String startDate;
    private int page;
    private int size;
    private String sort;
}