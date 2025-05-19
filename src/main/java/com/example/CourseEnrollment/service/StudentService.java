package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.StudentDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.repository.StudentRepository;
import com.example.CourseEnrollment.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Interface
public interface StudentService {
    // For REST API
    StudentDTO saveStudent(StudentDTO studentDTO);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();
    
    // For HTML View (Thymeleaf)
    Student createStudent(Student student);
    Student updateStudent(Long id, Student student);
    Student getStudentByIdEntity(Long id);
    List<Student> getAllStudentsEntity();

    void deleteStudent(Long id);
}


// Implementation
@Service
class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // REST API
    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
        }
        Student student = DTOMapper.toStudentEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return DTOMapper.toStudentDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        existingStudent.setName(studentDTO.getName());
        existingStudent.setEmail(studentDTO.getEmail());

        Student updatedStudent = studentRepository.save(existingStudent);
        return DTOMapper.toStudentDTO(updatedStudent);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return DTOMapper.toStudentDTO(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(DTOMapper::toStudentDTO)
                .collect(Collectors.toList());
    }

    // ThymeLeaf Support
    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        return studentRepository.save(existingStudent);
    }

    @Override
    public Student getStudentByIdEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    public List<Student> getAllStudentsEntity() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }
}