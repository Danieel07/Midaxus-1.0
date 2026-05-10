package com.example.Midaxus.services;

import com.example.Midaxus.model.dtos.UserDTO;
import com.example.Midaxus.model.entities.Admin;
import com.example.Midaxus.model.entities.Student;
import com.example.Midaxus.model.entities.Teacher;
import com.example.Midaxus.model.entities.User;
import com.example.Midaxus.model.mapper.TeacherMapper;
import com.example.Midaxus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUser<UserDTO, String> {


        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;


    @Override
    public UserDTO createUser(UserDTO dto) {

        switch (dto.getUserType()) {

            case "TEACHER":
                Teacher teacher = new Teacher();
                teacher.setTeacherCode(dto.getTeacherCode());
                teacher.setUserName(dto.getUserName());
                teacher.setFirstName(dto.getFirstName());
                teacher.setLastName(dto.getLastName());
                teacher.setEmail(dto.getEmail());
                teacher.setPassword(passwordEncoder.encode(dto.getPassword()));

                Teacher savedTeacher = userRepository.save(teacher);
                return new UserDTO(
                        "TEACHER",
                        savedTeacher.getTeacherCode(),
                        null,
                        null,
                        savedTeacher.getUserName(),
                        savedTeacher.getFirstName(),
                        savedTeacher.getLastName(),
                        savedTeacher.getEmail(),
                        null
                );


            case "STUDENT":
                Student student = new Student();
                student.setStudentId(dto.getStudentId());
                student.setUserName(dto.getUserName());
                student.setFirstName(dto.getFirstName());
                student.setLastName(dto.getLastName());
                student.setEmail(dto.getEmail());
                student.setPassword(passwordEncoder.encode(dto.getPassword()));

                Student savedStudent = userRepository.save(student);
                return new UserDTO(
                        "STUDENT",
                        null,
                        savedStudent.getStudentId(),
                        null,
                        savedStudent.getUserName(),
                        savedStudent.getFirstName(),
                        savedStudent.getLastName(),
                        savedStudent.getEmail(),
                        null
                );


            case "ADMIN":
                Admin admin = new Admin();
                admin.setAdminId(dto.getAdminId());
                admin.setUserName(dto.getUserName());
                admin.setFirstName(dto.getFirstName());
                admin.setLastName(dto.getLastName());
                admin.setEmail(dto.getEmail());
                admin.setPassword(passwordEncoder.encode(dto.getPassword()));

                Admin savedAdmin = userRepository.save(admin);
                return new UserDTO(
                        "ADMIN",
                        null,
                        null,
                        savedAdmin.getAdminId(),
                        savedAdmin.getUserName(),
                        savedAdmin.getFirstName(),
                        savedAdmin.getLastName(),
                        savedAdmin.getEmail(),
                        null
                );

            default:
                throw new RuntimeException("Tipo inválido");
        }
    }

    @Override
    public void deleteUser(String s) {
        if (userRepository.existsById(s)){
            userRepository.deleteById(s);
        }
    }

    @Override
    public UserDTO getUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        if (user instanceof Teacher t) {
            return new UserDTO("TEACHER", t.getTeacherCode(), null, null,
                    t.getUserName(), t.getFirstName(), t.getLastName(), t.getEmail(), null);
        }

        if (user instanceof Student s) {
            return new UserDTO("STUDENT", null, s.getStudentId(), null,
                    s.getUserName(), s.getFirstName(), s.getLastName(), s.getEmail(), null);
        }

        if (user instanceof Admin a) {
            return new UserDTO("ADMIN", null, null, a.getAdminId(),
                    a.getUserName(), a.getFirstName(), a.getLastName(), a.getEmail(), null);
        }

        return null;
    }
    }

