package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.service.impl.CourseServiceImpl;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course createTestCourse(Long id, String title, String description) {
        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setDescription(description);
        return course;
    }

    private CourseDTO createTestCourseDTO(Long id, String title, String description) {
        return new CourseDTO(id, title, description);
    }

    @Test
    void getAllCourses_shouldReturnListOfCourses() {
        // Arrange
        Course course1 = createTestCourse(1L, "Java", "Java Fundamentals");
        Course course2 = createTestCourse(2L, "Spring", "Spring Framework");
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        // Act
        List<CourseDTO> result = courseService.getAllCourses();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());
        assertEquals("Spring", result.get(1).getTitle());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getCourseById_shouldReturnCourseWhenExists() {
        // Arrange
        Long courseId = 1L;
        Course course = createTestCourse(courseId, "Java", "Java Fundamentals");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        CourseDTO result = courseService.getCourseById(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals("Java", result.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
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
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void createCourse_shouldSaveAndReturnNewCourse() {
        // Arrange
        CourseDTO newCourseDTO = createTestCourseDTO(null, "React", "React Basics");
        Course savedCourse = createTestCourse(1L, "React", "React Basics");
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // Act
        CourseDTO result = courseService.createCourse(newCourseDTO);

        // Assert
        assertNotNull(result.getId());
        assertEquals("React", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_shouldUpdateExistingCourse() {
        // Arrange
        Long courseId = 1L;
        Course existingCourse = createTestCourse(courseId, "Java", "Old Description");
        CourseDTO updateDTO = createTestCourseDTO(null, "Java", "Updated Description");
        Course updatedCourse = createTestCourse(courseId, "Java", "Updated Description");
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        // Act
        CourseDTO result = courseService.updateCourse(courseId, updateDTO);

        // Assert
        assertEquals(courseId, result.getId());
        assertEquals("Updated Description", result.getDescription());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void updateCourse_shouldThrowWhenNotFound() {
        // Arrange
        Long courseId = 99L;
        CourseDTO updateDTO = createTestCourseDTO(null, "Non-existent", "Course");
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.updateCourse(courseId, updateDTO);
        });
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void deleteCourse_shouldDeleteWhenExists() {
        // Arrange
        Long courseId = 1L;
        Course existingCourse = createTestCourse(courseId, "Java", "Description");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        doNothing().when(courseRepository).delete(existingCourse);

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(existingCourse);
    }

    @Test
    void deleteCourse_shouldThrowWhenNotFound() {
        // Arrange
        Long courseId = 99L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.deleteCourse(courseId);
        });
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, never()).delete(any(Course.class));
    }
}
