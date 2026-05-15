package com.example.Midaxus.model.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String courseGroupId;
    private LocalDate date;
    private boolean present;

    public Attendance() {}

    public Attendance(String studentId, String courseGroupId, LocalDate date, boolean present) {
        this.studentId = studentId;
        this.courseGroupId = courseGroupId;
        this.date = date;
        this.present = present;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseGroupId() { return courseGroupId; }
    public void setCourseGroupId(String courseGroupId) { this.courseGroupId = courseGroupId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
}
