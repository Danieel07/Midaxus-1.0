package com.example.Midaxus.services;

import com.example.Midaxus.model.dtos.EnrollmentDTO;
import com.example.Midaxus.model.dtos.StudentDTO;
import com.example.Midaxus.model.entities.CourseGroup;
import com.example.Midaxus.model.entities.Enrollment;
import com.example.Midaxus.model.entities.Student;
import com.example.Midaxus.model.enums.EnrollmentStatus;
import com.example.Midaxus.model.mapper.EnrollmentMapper;
import com.example.Midaxus.model.mapper.StudentMapper;
import com.example.Midaxus.repositories.CourseGroupRepository;
import com.example.Midaxus.repositories.EnrollmentRepository;
import com.example.Midaxus.model.entities.InstitutionPolicy;
import com.example.Midaxus.repositories.InstitutionPolicyRepository;
import com.example.Midaxus.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService implements IEnrollment<EnrollmentDTO, String> {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseGroupRepository courseGroupRepository;

    @Autowired
    private InstitutionPolicyRepository policyRepository;

    public List<StudentDTO> getStudentsByCourseGroup(String courseGroupId) {
        CourseGroup courseGroup = courseGroupRepository.findById(courseGroupId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        return enrollmentRepository.getAllByCourseGroup(courseGroup).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ENROLLED)
                .map(e -> StudentMapper.toDTO(e.getStudent()))
                .toList();
    }

    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO dto) {

        if (dto == null) throw new RuntimeException("Datos inválidos");

        // buscar entidades por ID
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseGet(() -> studentRepository.findAll().stream()
                        .filter(s -> 
                            (s.getStudentId() != null && s.getStudentId().equals(dto.getStudentId())) ||
                            (s.getId() != null && s.getId().equals(dto.getStudentId())) ||
                            (s.getEmail() != null && s.getEmail().equals(dto.getStudentId()))
                        )
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Student no encontrado con el ID: " + dto.getStudentId())));

        CourseGroup courseGroup = courseGroupRepository.findById(dto.getCourseGroupId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        //  evitar duplicado
        if (enrollmentRepository.existsByStudentAndCourseGroup(student, courseGroup)) {
            throw new RuntimeException("Ya estás inscrito en este curso");
        }

        InstitutionPolicy policy = policyRepository.findById(1L).orElseGet(() -> {
            InstitutionPolicy p = new InstitutionPolicy();
            p.setStandardCapacity(40);
            p.setCapacityTolerancePercent(0);
            return p;
        });

        int stdCap = policy.getStandardCapacity() != null ? policy.getStandardCapacity() : 40;
        int tolPct = policy.getCapacityTolerancePercent() != null ? policy.getCapacityTolerancePercent() : 0;

        //  capacidad (HU-14: control de aforo por grupo con un estándar y tolerancia)
        int current = enrollmentRepository.getAllByCourseGroup(courseGroup).size();
        int baseCapacity = courseGroup.getCapacity() > 0 ? courseGroup.getCapacity() : stdCap;
        int maxAllowed = baseCapacity + (baseCapacity * tolPct / 100);

        if (maxAllowed <= 0) maxAllowed = 40; // Fallback por seguridad

        if (current >= maxAllowed) {
            throw new RuntimeException("Curso lleno (aforo máximo alcanzado)");
        }

        //  crear enrollment
        Enrollment enrollment = EnrollmentMapper.toEntity(dto);

        enrollment.setStudent(student);
        enrollment.setCourseGroup(courseGroup);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        Enrollment saved = enrollmentRepository.save(enrollment);

        return EnrollmentMapper.toDTO(saved);
    }

    @Override
    public EnrollmentDTO getEnrollment(String id) {
        Enrollment e = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado"));

        return EnrollmentMapper.toDTO(e);
    }

    @Override
    public List<EnrollmentDTO> getAll() {
        return EnrollmentMapper.toDTOList(
                enrollmentRepository.findAllByStatus(EnrollmentStatus.ENROLLED)
        );
    }

    @Override
    public void deleteEnrollment(String id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment no encontrado"));

        if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
            throw new RuntimeException("El estudiante ya se retiró de este curso");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);

        enrollmentRepository.save(enrollment);
    }


}