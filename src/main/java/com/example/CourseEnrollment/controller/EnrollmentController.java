package com.example.CourseEnrollment.controller;

import com.example.CourseEnrollment.dto.EnrollmentDTO;
import com.example.CourseEnrollment.model.Enrollment;
import com.example.CourseEnrollment.service.CourseService;
import com.example.CourseEnrollment.service.EnrollmentService;
import com.example.CourseEnrollment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // Thymeleaf - List View
    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllEnrollmentEntities());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("enrollment", new EnrollmentDTO());
        return "enrollments";
    }

    // Thymeleaf - Add
    @PostMapping("/add")
    public String addEnrollment(@ModelAttribute("enrollment") EnrollmentDTO enrollmentDTO) {
        enrollmentService.enrollStudent(enrollmentDTO);
        return "redirect:/enrollments";
    }

    // Thymeleaf - Edit
    @GetMapping("/edit/{id}")
    public String editEnrollment(@PathVariable Long id, Model model) {
        Enrollment enrollment = enrollmentService.getEnrollmentEntityById(id);

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setStatus(enrollment.getStatus());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());

        model.addAttribute("enrollment", dto);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "edit_enrollpage";
    }

    @PostMapping("/update/{id}")
    public String updateEnrollment(@PathVariable Long id, @ModelAttribute("enrollment") EnrollmentDTO enrollmentDTO) {
        enrollmentService.updateEnrollment(id, enrollmentDTO);
        return "redirect:/enrollments";
    }

    // Thymeleaf - Delete
    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/enrollments";
    }

    // -----REST APIs----

    @RestController
    @RequestMapping("/api/enrollments")
    public static class EnrollmentRestController {

        @Autowired
        private EnrollmentService enrollmentService;

        // REST API - Add Enrollment
        @PostMapping
        public ResponseEntity<EnrollmentDTO> enrollStudent(@RequestBody EnrollmentDTO enrollmentDTO) {
            return ResponseEntity.status(201).body(enrollmentService.enrollStudent(enrollmentDTO));
        }

        // REST API - Get All Enrollments
        @GetMapping
        public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
            return ResponseEntity.ok(enrollmentService.getAllEnrollments());
        }

        // REST API - Get Enrollment by ID
        @GetMapping("/{id}")
        public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
            return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
        }

        // REST API - Update Enrollment
        @PutMapping("/{id}")
        public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long id, @RequestBody EnrollmentDTO dto) {
            return ResponseEntity.ok(enrollmentService.updateEnrollment(id, dto));
        }

        // REST API - Delete Enrollment
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteEnrollmentApi(@PathVariable Long id) {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        }
    }
}