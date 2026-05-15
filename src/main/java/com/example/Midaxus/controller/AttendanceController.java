package com.example.Midaxus.controller;

import com.example.Midaxus.model.entities.Attendance;
import com.example.Midaxus.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository repository;

    @GetMapping("/course/{courseGroupId}")
    public ResponseEntity<List<Attendance>> getAttendance(@PathVariable String courseGroupId, @RequestParam(required = false) String date) {
        LocalDate localDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return ResponseEntity.ok(repository.findByCourseGroupIdAndDate(courseGroupId, localDate));
    }

    @PostMapping
    public ResponseEntity<Attendance> saveAttendance(@RequestBody Attendance attendance) {
        if (attendance.getDate() == null) attendance.setDate(LocalDate.now());
        
        Optional<Attendance> existing = repository.findByStudentIdAndCourseGroupIdAndDate(
                attendance.getStudentId(), attendance.getCourseGroupId(), attendance.getDate());
        
        if (existing.isPresent()) {
            Attendance update = existing.get();
            update.setPresent(attendance.isPresent());
            return ResponseEntity.ok(repository.save(update));
        }
        
        return ResponseEntity.ok(repository.save(attendance));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Attendance>> saveBatchAttendance(@RequestBody List<Attendance> attendances) {
        LocalDate now = LocalDate.now();
        attendances.forEach(a -> {
            if (a.getDate() == null) a.setDate(now);
            Optional<Attendance> existing = repository.findByStudentIdAndCourseGroupIdAndDate(
                    a.getStudentId(), a.getCourseGroupId(), a.getDate());
            existing.ifPresent(value -> a.setId(value.getId()));
        });
        return ResponseEntity.ok(repository.saveAll(attendances));
    }
}
