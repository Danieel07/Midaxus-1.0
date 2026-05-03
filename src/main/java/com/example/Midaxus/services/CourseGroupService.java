package com.example.Midaxus.services;

import com.example.Midaxus.model.dtos.CourseGroupDTO;
import com.example.Midaxus.model.entities.*;
import com.example.Midaxus.model.enums.EnrollmentStatus;
import com.example.Midaxus.model.mapper.CourseGroupMapper;
import com.example.Midaxus.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseGroupService implements ICourseGroup<CourseGroupDTO, String> {

    @Autowired
    private CourseGroupRepository courseGroupRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AcademicPeriodRepository academicPeriodRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 🔹 CREATE
    @Override
    public CourseGroupDTO create(CourseGroupDTO dto) {

        if (dto == null) throw new RuntimeException("Datos inválidos");


        Teacher teacher = teacherRepository.findByTeacherCode(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher no encontrado"));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject no encontrado"));

        AcademicPeriod period = null;
        if (dto.getAcademicPeriodId() != null && !dto.getAcademicPeriodId().isEmpty()) {
            period = academicPeriodRepository.findById(dto.getAcademicPeriodId())
                    .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));
        }
        CourseGroup entity = CourseGroupMapper.toEntity(dto);
        if (entity.getCourseGroupId() == null || entity.getCourseGroupId().isEmpty()) {
            entity.setCourseGroupId(java.util.UUID.randomUUID().toString());
        }

        entity.setTeacher(teacher);
        entity.setSubject(subject);
        entity.setAcademicPeriod(period);

        CourseGroup saved = courseGroupRepository.save(entity);

        return CourseGroupMapper.toDTO(saved);
    }

    @Override
    public CourseGroupDTO update(String id, CourseGroupDTO dto) {
        if (dto == null) throw new RuntimeException("Datos inválidos");

        CourseGroup existing = courseGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseGroup no encontrado"));

        Teacher teacher = null;
        if (dto.getTeacherId() != null && !dto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findByTeacherCode(dto.getTeacherId())
                    .orElseGet(() -> teacherRepository.findById(dto.getTeacherId()).orElse(null));
        }

        Subject subject = existing.getSubject();
        if (dto.getSubjectId() != null) {
            subject = subjectRepository.findById(dto.getSubjectId()).orElse(existing.getSubject());
        }

        AcademicPeriod period = existing.getAcademicPeriod();
        if (dto.getAcademicPeriodId() != null) {
            period = academicPeriodRepository.findById(dto.getAcademicPeriodId()).orElse(existing.getAcademicPeriod());
        }

        existing.setTeacher(teacher);
        existing.setSubject(subject);
        existing.setAcademicPeriod(period);
        existing.setCapacity(dto.getCapacity());
        existing.setCode(dto.getCode());

        CourseGroup updated = courseGroupRepository.save(existing);
        return CourseGroupMapper.toDTO(updated);
    }


    @Override
    public CourseGroupDTO getById(String id) {

        CourseGroup cg = courseGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseGroup no encontrado"));

        return CourseGroupMapper.toDTO(cg);
    }

    // 🔹 GET ALL
    @Override
    public List<CourseGroupDTO> getAll() {
        return CourseGroupMapper.toDTOList(courseGroupRepository.findAll());
    }

    // 🔹 DELETE
    @Override
    public void delete(String id) {

        if (!courseGroupRepository.existsById(id)) {
            throw new RuntimeException("CourseGroup no existe");
        }

        courseGroupRepository.deleteById(id);
    }

    // 🔹 GET BY TEACHER
    @Override
    public List<CourseGroupDTO> getByTeacher(String teacherId) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher no encontrado"));

        return CourseGroupMapper.toDTOList(
                courseGroupRepository.findAllByTeacher(teacher)
        );
    }

    // 🔹 GET BY SUBJECT
    @Override
    public List<CourseGroupDTO> getBySubject(String subjectId) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject no encontrado"));

        return CourseGroupMapper.toDTOList(
                courseGroupRepository.findAllBySubject(subject)
        );
    }

    //Metodo para que el profesor pueda ver cursos tiene asignados
    @Override
    public List<CourseGroupDTO> getCoursesByTeacher(String teacherId) {
        return CourseGroupMapper.toDTOList(
                courseGroupRepository.findByTeacher_TeacherCode(teacherId)
        );
    }

    @Override
    public List<CourseGroupDTO> getCoursesByStudent(String studentId) {

        // Intentar buscar por studentId primero
        List<Enrollment> enrollments = enrollmentRepository
                .findByStudent_StudentIdAndStatus(studentId, EnrollmentStatus.ENROLLED);

        // Si no encontró nada, intentar buscar al estudiante por su UUID (id de User)
        if (enrollments.isEmpty()) {
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student != null && student.getStudentId() != null) {
                enrollments = enrollmentRepository
                        .findByStudent_StudentIdAndStatus(student.getStudentId(), EnrollmentStatus.ENROLLED);
            }
        }

        // Si aún no encontró nada, intentar buscar por email (fallback final)
        if (enrollments.isEmpty()) {
            Student student = studentRepository.findAll().stream()
                    .filter(s -> s.getEmail() != null && s.getEmail().equals(studentId))
                    .findFirst()
                    .orElse(null);
            if (student != null && student.getStudentId() != null) {
                enrollments = enrollmentRepository
                        .findByStudent_StudentIdAndStatus(student.getStudentId(), EnrollmentStatus.ENROLLED);
            }
        }

        return enrollments.stream()
                .map(Enrollment::getCourseGroup)
                .map(CourseGroupMapper::toDTO)
                .toList();
    }


}