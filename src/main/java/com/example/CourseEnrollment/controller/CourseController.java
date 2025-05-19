package com.example.CourseEnrollment.controller;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Web (Thymeleaf) Endpoints

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCoursesModel());
        model.addAttribute("course", new Course());
        return "courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute("courses") Course course) {
        courseService.createCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        Course existingCourse = courseService.getCourseByIdModel(id);
        model.addAttribute("course", existingCourse);
        model.addAttribute("courses", courseService.getAllCoursesModel());
        return "courses";
    }

    @PostMapping("/courses/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("courses") Course course) {
        courseService.updateCourseModel(id, course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    // REST API Endpoints

    @RestController
    @RequestMapping("/api/courses")
    static class CourseRestController {

        @Autowired
        private CourseService courseService;

        @PostMapping
        public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
            return courseService.saveCourse(courseDTO);
        }

        @GetMapping("/{id}")
        public CourseDTO getCourseById(@PathVariable Long id) {
            return courseService.getCourseById(id);
        }

        @GetMapping
        public List<CourseDTO> getAllCourses() {
            return courseService.getAllCourses();
        }

        @PutMapping("/{id}")
        public CourseDTO updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
            return courseService.updateCourse(id, courseDTO);
        }

        @DeleteMapping("/{id}")
        public void deleteCourse(@PathVariable Long id) {
            courseService.deleteCourse(id);
        }
    }
}