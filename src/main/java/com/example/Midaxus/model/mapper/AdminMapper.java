package com.example.midaxus.model.mapper;

import com.example.midaxus.model.dtos.AdminDto;
import com.example.midaxus.model.dtos.StudentDto;
import com.example.midaxus.model.entities.Admin;
import com.example.midaxus.model.entities.Student;

import java.util.Collections;
import java.util.List;

public class AdminMapper {


  public static AdminDto toDto(Admin admin){
    if (admin == null) {
  return null;
  }

    return new AdminDto(admin.getAdminId(),
    admin.getFirstName(),
    admin.getLastName(),
    admin.getUserName(),
    admin.getEmail());
  }


  public static List<AdminDto> toDtolist(List<Admin>admins){

    if (admins == null) {
  return Collections.emptyList();
  }

    return admins.stream().map(AdminMapper::toDto).toList();
  }


}






















