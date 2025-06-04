package com.example.CourseEnrollment.controller;

import com.example.CourseEnrollment.dto.StudentDTO;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // --- HTML View Controllers (Thymeleaf) ---

    @GetMapping   // Get all students
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("student", new Student());
        return "students";
    }

    @PostMapping("/add")  // Add new Student
    public String addStudent(@ModelAttribute("students") Student student) {  //
        studentService.createStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}") // Get Student by id
    public String editStudent(@PathVariable Long id, Model model) {
        StudentDTO existingStudent = studentService.getStudentById(id);
        model.addAttribute("student", existingStudent);
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @PostMapping("/update/{id}")   // Update by id                                
    public String updateStudent(@PathVariable Long id, @ModelAttribute("students") Student student) {
        student.setId(id);
        studentService.updateStudent(id, student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")  // Delete Student
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    // --- REST API Controllers  ---------------------------------------------------

    @RestController
    @RequestMapping("/api/students")
    public static class StudentRestController {

        @Autowired
        private StudentService studentService;

        // Add Student
        @PostMapping
        public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
            StudentDTO createdStudent = studentService.saveStudent(studentDTO);
            return ResponseEntity.status(201).body(createdStudent);
        }
        
        // Get All Students   
        @GetMapping
        public ResponseEntity<List<StudentDTO>> getAllStudents() {
            List<StudentDTO> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        }
        
        // Get Students by id
        @GetMapping("/{id}")
        public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
            StudentDTO studentDTO = studentService.getStudentById(id);
            return ResponseEntity.ok(studentDTO);
        }
        
        // Update Student by id
        @PutMapping("/{id}")
        public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
            StudentDTO updatedStudent = studentService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(updatedStudent);
        }

        // Delete Student by id
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        }
    }
}