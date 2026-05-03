package com.example.Midaxus.services;

import com.example.Midaxus.model.dtos.StudentDTO;
import com.example.Midaxus.model.entities.Student;
import com.example.Midaxus.model.entities.Teacher;
import com.example.Midaxus.model.mapper.StudentMapper;
import com.example.Midaxus.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudent<String, StudentDTO> {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);
        if (student.getPassword() != null) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        Student saved = studentRepository.save(student);
        return StudentMapper.toDTO(saved);
    }

    @Override
    public StudentDTO getStudent(String s) {
        Student student = studentRepository.getReferenceById(s);
        return StudentMapper.toDTO(student);

    }

    @Override
    public void deleteStudent(StudentDTO studentDTO) {
        studentRepository.delete(StudentMapper.toEntity(studentDTO));

    }

    @Override
    public List<StudentDTO> getStudents() {

        return studentRepository.findAll().stream().map(StudentMapper::toDTO).toList();
    }
}
