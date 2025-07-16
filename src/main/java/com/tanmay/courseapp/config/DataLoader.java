package com.tanmay.courseapp.config;

import com.tanmay.courseapp.document.CourseDocument;
import com.tanmay.courseapp.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        courseRepository.deleteAll();
        try (InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream()) {
            CourseDocument[] courses = objectMapper.readValue(inputStream, CourseDocument[].class);
            courseRepository.saveAll(Arrays.asList(courses));
        }
    }
}