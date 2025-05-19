package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.service.impl.CourseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Course courseEntity(Long id, String title, String desc) {
        return new Course(id, title, desc);
    }

    private CourseDTO courseDTO(Long id, String title, String desc) {
        return new CourseDTO(id, title, desc);
    }

    @Test
    void getAllCourses_shouldReturnCourseDTOList() {
        Course c1 = courseEntity(1L, "Java", "Java Basics");
        Course c2 = courseEntity(2L, "Spring", "Spring Intro");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CourseDTO> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }

    @Test
    void getCourseById_shouldReturnCourseDTO() {
        Course c = courseEntity(1L, "Java", "Java Basics");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(c));

        CourseDTO result = courseService.getCourseById(1L);

        assertEquals("Java", result.getTitle());
    }

    @Test
    void getCourseById_shouldThrowIfNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(99L);
        });
    }

    @Test
    void createCourse_shouldReturnCreatedDTO() {
        CourseDTO dto = courseDTO(null, "Spring Boot", "Backend course");
        Course saved = courseEntity(1L, "Spring Boot", "Backend course");

        when(courseRepository.save(any(Course.class))).thenReturn(saved);

        CourseDTO result = courseService.createCourse(dto);

        assertEquals(1L, result.getId());
        assertEquals("Spring Boot", result.getTitle());
    }

    @Test
    void updateCourse_shouldReturnUpdatedDTO() {
        Course existing = courseEntity(1L, "Java", "Old Desc");
        Course updated = courseEntity(1L, "Java", "New Desc");

        CourseDTO updateDTO = courseDTO(null, "Java", "New Desc");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenReturn(updated);

        CourseDTO result = courseService.updateCourse(1L, updateDTO);

        assertEquals("New Desc", result.getDescription());
    }

    @Test
    void deleteCourse_shouldCallRepository() {
        Course c = courseEntity(1L, "Java", "To delete");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(c));

        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }
}
