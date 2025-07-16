package com.tanmay.courseapp.controller;

import com.tanmay.courseapp.dto.SearchRequestDTO;
import com.tanmay.courseapp.dto.SearchResponseDTO;
import com.tanmay.courseapp.service.CourseService;
import com.tanmay.courseapp.document.CourseDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public SearchResponseDTO search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "relevance") String sort
    ) {
        SearchRequestDTO request = new SearchRequestDTO();
        request.setQuery(q);
        request.setCategory(category);
        request.setMinAge(minAge);
        request.setMaxAge(maxAge);
        request.setType(type);
        request.setStartDate(startDate);
        request.setPage(page);
        request.setSize(size);
        request.setSort(sort);

        SearchHits<CourseDocument> searchHits = courseService.searchCourses(request);
        List<CourseDocument> courses = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        SearchResponseDTO response = new SearchResponseDTO();
        response.setTotal(searchHits.getTotalHits());
        response.setCourses(courses);
        return response;
    }

    @GetMapping("/suggest")
    public List<String> autocomplete(@RequestParam String q) {
        return courseService.autocompleteTitle(q);
    }

}