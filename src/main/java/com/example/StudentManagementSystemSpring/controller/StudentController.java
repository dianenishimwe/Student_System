package com.example.StudentManagementSystemSpring.controller;

import com.example.StudentManagementSystemSpring.model.Student;
import com.example.StudentManagementSystemSpring.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student, Model model) {
        studentService.registerStudent(student);
        model.addAttribute("successMessage", "Student registered successfully!");
        model.addAttribute("student", new Student());
        return "register";
    }

    @GetMapping("/all")
    public String showAllStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/{id}")
    public String showStudentById(@PathVariable int id, Model model) {
        Student student = studentService.findStudentById(id);

        if (student == null) {
            model.addAttribute("message", "Student not found with ID: " + id);
            model.addAttribute("students", java.util.Collections.emptyList());
        } else {
            model.addAttribute("students", java.util.Collections.singletonList(student));
        }

        return "students";
    }
}