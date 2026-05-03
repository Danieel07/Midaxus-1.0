package com.example.Midaxus.controller;

import com.example.Midaxus.model.dtos.StudentDTO;
import com.example.Midaxus.services.IStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private IStudent<String, StudentDTO> studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        StudentDTO created = studentService.createStudent(dto);
        return ResponseEntity
                .created(URI.create("/api/students/" + created.getStudentId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }
}
