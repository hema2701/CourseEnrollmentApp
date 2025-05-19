package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Interface
public interface CourseService {
    // For REST API
    CourseDTO saveCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    CourseDTO getCourseById(Long id);
    List<CourseDTO> getAllCourses();
    void deleteCourse(Long id);

    // For Thymeleaf Web UI
    List<Course> getAllCoursesModel();
    Course getCourseByIdModel(Long id);
    void createCourse(Course course);
    void updateCourseModel(Long id, Course updatedCourse);
}

// Implementation
@Service
class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // ====== For REST API (DTO) ======

    @Override
    public CourseDTO saveCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());

        Course savedCourse = courseRepository.save(course);
        return DTOMapper.toCourseDTO(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        existingCourse.setTitle(courseDTO.getTitle());
        existingCourse.setDescription(courseDTO.getDescription());

        Course updatedCourse = courseRepository.save(existingCourse);
        return DTOMapper.toCourseDTO(updatedCourse);
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        return DTOMapper.toCourseDTO(course);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(DTOMapper::toCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    // ====== For Thymeleaf Web UI (Model Entity) ======

    @Override
    public List<Course> getAllCoursesModel() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseByIdModel(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
    }

    @Override
    public void createCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void updateCourseModel(Long id, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        courseRepository.save(existingCourse);
    }
}