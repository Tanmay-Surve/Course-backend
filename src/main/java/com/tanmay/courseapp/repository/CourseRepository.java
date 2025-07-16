package com.tanmay.courseapp.repository;

import com.tanmay.courseapp.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {
}
