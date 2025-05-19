package com.example.CourseEnrollment.controller;

import com.example.CourseEnrollment.service.CourseService;
import com.example.CourseEnrollment.service.EnrollmentService;
import com.example.CourseEnrollment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("enrollmentCount", enrollmentService.getAllEnrollments().size());
        return "home";
    }
}