package com.tanmay.courseapp.service;

import com.tanmay.courseapp.document.CourseDocument;
import com.tanmay.courseapp.dto.SearchRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public SearchHits<CourseDocument> searchCourses(SearchRequestDTO request) {
        Criteria criteria = new Criteria();

        // Full-text query
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            criteria = criteria.or("title").contains(request.getQuery())
                    .or("description").contains(request.getQuery());
        }

        // Category exact match
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            criteria = criteria.and("category").is(request.getCategory());
        }

        // Age range
        if (request.getMinAge() != null) {
            criteria = criteria.and("minAge").greaterThanEqual(request.getMinAge());
        }
        if (request.getMaxAge() != null) {
            criteria = criteria.and("maxAge").lessThanEqual(request.getMaxAge());
        }

        // Type exact match
        if (request.getType() != null && !request.getType().isEmpty()) {
            criteria = criteria.and("type").is(request.getType());
        }

        // Start date filtering
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            try {
                Instant startInstant = Instant.parse(request.getStartDate());
                criteria = criteria.and("nextSessionDate").greaterThanEqual(startInstant.toString());
            } catch (DateTimeParseException e) {
                System.err.println("Invalid startDate format: " + request.getStartDate());
            }
        }

        // Sorting logic
        Sort sort;
        switch (request.getSort().toLowerCase()) {
            case "priceasc":
                sort = Sort.by("price").ascending();
                break;
            case "pricedesc":
                sort = Sort.by("price").descending();
                break;
            case "upcoming":
            default:
                sort = Sort.by("nextSessionDate").ascending(); // Default to soonest first
                break;
        }

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), sort);
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageRequest);

        return elasticsearchOperations.search(query, CourseDocument.class);
    }

    public List<String> autocompleteTitle(String input) {
        Criteria criteria = new Criteria("title").startsWith(input);
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setMaxResults(10);

        SearchHits<CourseDocument> hits = elasticsearchOperations.search(query, CourseDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().getTitle())
                .distinct()
                .collect(Collectors.toList());
    }

}
