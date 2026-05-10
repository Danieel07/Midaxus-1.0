package com.example.Midaxus.controller;

import com.example.Midaxus.model.dtos.CourseGroupDTO;
import com.example.Midaxus.model.dtos.EnrollmentDTO;
import com.example.Midaxus.model.dtos.StudentDTO;
import com.example.Midaxus.services.CourseGroupService;
import com.example.Midaxus.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private CourseGroupService courseGroupService;

    // GET STUDENTS BY COURSE
    @GetMapping("/course/{courseGroupId}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsByCourse(@PathVariable String courseGroupId) {
        return ResponseEntity.ok(enrollmentService.getStudentsByCourseGroup(courseGroupId));
    }

    //  CREATE (inscribir estudiante)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody EnrollmentDTO dto) {
        System.out.println("=== LLEGO PETICION DE ENROLLMENT ===");
        System.out.println("StudentId: " + dto.getStudentId());
        System.out.println("CourseGroupId: " + dto.getCourseGroupId());
        
        try {
            EnrollmentDTO created = enrollmentService.createEnrollment(dto);

            return ResponseEntity
                    .created(URI.create("/api/enrollments/" + created.getEnrollmentId()))
                    .body(created);
        } catch (Throwable e) {
            System.out.println("=== ERROR EN ENROLLMENT ===");
            e.printStackTrace(); // Log in Docker
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  GET ALL
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAll() {
        return ResponseEntity.ok(enrollmentService.getAll());
    }

    //  GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.getEnrollment(id));
    }

    //  DELETE (soft delete → DROPPED)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    // GET COURSES BY STUDENT
    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<CourseGroupDTO>> getCoursesByStudent(@PathVariable String studentId) {

        return ResponseEntity.ok(courseGroupService.getCoursesByStudent(studentId));
    }
}