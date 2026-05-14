package com.example.Midaxus.model.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "course_group")
public class CourseGroup {

    @Id
    private String courseGroupId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private String code;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "academic_period_id")
    private AcademicPeriod academicPeriod;
    private int capacity;

    @OneToMany(mappedBy = "courseGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private  List<Enrollment>enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "courseGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleSession> scheduleSessions = new ArrayList<>();

    @OneToMany(mappedBy = "courseGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentScheduleSlot> studentScheduleSlots = new ArrayList<>();


    public CourseGroup(){}

    public CourseGroup(String courseGroupId, Teacher teacher, String code, Subject subject, AcademicPeriod academicPeriod, int capacity, List<Enrollment> enrollments, List<Enrollment> enrollments1) {
        this.courseGroupId = courseGroupId;
        this.teacher = teacher;
        this.code = code;
        this.subject = subject;
        this.academicPeriod = academicPeriod;
        this.capacity = capacity;
        this.enrollments = enrollments;
    }

    public String getCourseGroupId() {
        return courseGroupId;
    }

    public void setCourseGroupId(String courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public AcademicPeriod getAcademicPeriod() {
        return academicPeriod;
    }

    public void setAcademicPeriod(AcademicPeriod academicPeriod) {
        this.academicPeriod = academicPeriod;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    @Override
    public String toString() {
        return code; // o el atributo que identifique el curso
    }
}
