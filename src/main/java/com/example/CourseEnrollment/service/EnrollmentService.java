package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.EnrollmentDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.model.Enrollment;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.repository.EnrollmentRepository;
import com.example.CourseEnrollment.repository.StudentRepository;
import com.example.CourseEnrollment.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
public interface EnrollmentService {
    EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO);
    List<EnrollmentDTO> getAllEnrollments();
    EnrollmentDTO getEnrollmentById(Long id); // Get by ID
    EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO); // Update by ID
    void deleteEnrollment(Long id);

    // For Thymeleaf
    List<Enrollment> getAllEnrollmentEntities();
    Enrollment getEnrollmentEntityById(Long id);
    void createEnrollment(Enrollment enrollment);
    void updateEnrollmentEntity(Long id, Enrollment enrollment);
}

@Service
class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO) {
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + enrollmentDTO.getStudentId()));

        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + enrollmentDTO.getCourseId()));

        Enrollment enrollment = DTOMapper.toEnrollmentEntity(enrollmentDTO);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return DTOMapper.toEnrollmentDTO(savedEnrollment);
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(DTOMapper::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));
        return DTOMapper.toEnrollmentDTO(enrollment);
    }

    @Override
    public EnrollmentDTO updateEnrollment(Long id, EnrollmentDTO enrollmentDTO) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + id));

        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + enrollmentDTO.getStudentId()));

        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + enrollmentDTO.getCourseId()));

        existingEnrollment.setStudent(student);
        existingEnrollment.setCourse(course);
        existingEnrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        existingEnrollment.setStatus(enrollmentDTO.getStatus());

        Enrollment updatedEnrollment = enrollmentRepository.save(existingEnrollment);
        return DTOMapper.toEnrollmentDTO(updatedEnrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    
    // Thymeleaf Support
    @Override
    public List<Enrollment> getAllEnrollmentEntities() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentEntityById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    }

    @Override
    public void createEnrollment(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }

    @Override
    public void updateEnrollmentEntity(Long id, Enrollment updated) {
        Enrollment existing = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        existing.setStudent(updated.getStudent());
        existing.setCourse(updated.getCourse());
        existing.setStatus(updated.getStatus());
        existing.setEnrollmentDate(updated.getEnrollmentDate());
        enrollmentRepository.save(existing);
    }
}