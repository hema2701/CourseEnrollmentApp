package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
//import com.example.CourseEnrollment.util.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCourse_shouldReturnSavedCourseDTO() {
        Course course = new Course(1L, "Java", "Learn Java");
        when(courseRepository.save(any())).thenReturn(course);

        CourseDTO result = courseService.saveCourse(new CourseDTO(null, "Java", "Learn Java"));
        assertEquals("Java", result.getTitle());
    }

    @Test
    void getCourseById_shouldReturnCourseDTO() {
        Course course = new Course(1L, "Java", "Learn Java");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDTO result = courseService.getCourseById(1L);
        assertEquals("Java", result.getTitle());
    }

    @Test
    void getCourseById_shouldThrowIfNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(1L));
    }

    @Test
    void getAllCourses_shouldReturnList() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(new Course(1L, "Java", "Learn Java")));
        assertEquals(1, courseService.getAllCourses().size());
    }

    @Test
    void updateCourse_shouldUpdateAndReturn() {
        Course existing = new Course(1L, "Old", "Old Desc");
        Course updated = new Course(1L, "New", "New Desc");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any())).thenReturn(updated);

        CourseDTO result = courseService.updateCourse(1L, new CourseDTO(1L, "New", "New Desc"));
        assertEquals("New", result.getTitle());
    }

    @Test
    void deleteCourse_shouldCallRepository() {
        courseService.deleteCourse(1L);
        verify(courseRepository).deleteById(1L);
    }
}