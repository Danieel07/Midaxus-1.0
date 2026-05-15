package com.example.Midaxus.repositories;

import com.example.Midaxus.model.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    List<Attendance> findByCourseGroupIdAndDate(String courseGroupId, LocalDate date);
    Optional<Attendance> findByStudentIdAndCourseGroupIdAndDate(String studentId, String courseGroupId, LocalDate date);
}
