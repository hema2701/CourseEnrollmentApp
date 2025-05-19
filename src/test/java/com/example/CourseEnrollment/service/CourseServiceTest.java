package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
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
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCourses_shouldReturnCourseList() {
        Course c1 = new Course(1L, "Java", "Learn Java");
        Course c2 = new Course(2L, "Python", "Learn Python");
        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }

    @Test
    void getCourseById_shouldReturnCourse() {
        Course course = new Course(1L, "Java", "Learn Java");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
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
    void createCourse_shouldSaveAndReturnCourse() {
        Course course = new Course(null, "Spring Boot", "Learn Spring Boot");
        Course saved = new Course(1L, "Spring Boot", "Learn Spring Boot");

        when(courseRepository.save(course)).thenReturn(saved);

        Course result = courseService.createCourse(course);

        assertEquals(1L, result.getId());
        assertEquals("Spring Boot", result.getTitle());
    }

    @Test
    void updateCourse_shouldModifyAndReturnCourse() {
        Course existing = new Course(1L, "Java", "Old");
        Course updated = new Course(1L, "Java", "Updated");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(updated);

        Course result = courseService.updateCourse(1L, updated);

        assertEquals("Updated", result.getDescription());
    }

    @Test
    void deleteCourse_shouldCallRepository() {
        Course course = new Course(1L, "Java", "To Delete");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }
}
