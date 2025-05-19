package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course createCourse(Long id, String title, String description) {
        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setDescription(description);
        return course;
    }

    private CourseDTO createCourseDTO(Long id, String title, String description) {
        return new CourseDTO(id, title, description);
    }

    @Test
    void getAllCourses_shouldReturnAllCourses() {
        // Arrange
        Course course1 = createCourse(1L, "Java", "Java Basics");
        Course course2 = createCourse(2L, "Spring", "Spring Basics");
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        // Act
        List<CourseDTO> result = courseService.getAllCourses();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());
        assertEquals("Spring", result.get(1).getTitle());
    }

    @Test
    void getCourseById_shouldReturnCourseWhenExists() {
        // Arrange
        Long courseId = 1L;
        Course course = createCourse(courseId, "Java", "Java Basics");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        CourseDTO result = courseService.getCourseById(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals("Java", result.getTitle());
    }

    @Test
    void getCourseById_shouldThrowWhenNotFound() {
        // Arrange
        Long courseId = 99L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(courseId);
        });
    }

    @Test
    void createCourse_shouldSaveAndReturnCourse() {
        // Arrange
        CourseDTO newCourse = createCourseDTO(null, "React", "React Basics");
        Course savedCourse = createCourse(1L, "React", "React Basics");
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // Act
        CourseDTO result = courseService.createCourse(newCourse);

        // Assert
        assertNotNull(result.getId());
        assertEquals("React", result.getTitle());
    }

    @Test
    void updateCourse_shouldUpdateExistingCourse() {
        // Arrange
        Long courseId = 1L;
        Course existingCourse = createCourse(courseId, "Java", "Old Description");
        CourseDTO updateDTO = createCourseDTO(null, "Java", "Updated Description");
        Course updatedCourse = createCourse(courseId, "Java", "Updated Description");
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // Act
        CourseDTO result = courseService.updateCourse(courseId, updateDTO);

        // Assert
        assertEquals(courseId, result.getId());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void deleteCourse_shouldDeleteExistingCourse() {
        // Arrange
        Long courseId = 1L;
        Course existingCourse = createCourse(courseId, "Java", "Description");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        doNothing().when(courseRepository).delete(existingCourse);

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        verify(courseRepository, times(1)).delete(existingCourse);
    }
}
