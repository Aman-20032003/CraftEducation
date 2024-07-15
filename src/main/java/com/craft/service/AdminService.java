package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.controller.response.GlobalTeacherResponse;
import com.craft.repository.AdminRepository;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.Teacher;

import lombok.Builder;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	public ResponseEntity<AdminResponse> adminLogin(AdminLoginRequest adminLoginRequest) {

		Admin admin = adminRepository.findByLoginIdAndPassword(adminLoginRequest.getLoginId(),
				adminLoginRequest.getPassword());
		if (admin != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new AdminResponse("Login Successfully", true));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new AdminResponse("Login Failed !! Invalid Id or Password", false));
	}

//	TEACHER REGISTERATION
	public ResponseEntity<GlobalTeacherResponse> registerNewTeacher(
			TeacherRegisterationRequest teacherRegisterationRequest) {
		Teacher teacher = Teacher.builder().name(teacherRegisterationRequest.getName())
				.emailId(teacherRegisterationRequest.getEmailId())
				.aadharNumber(teacherRegisterationRequest.getAadharNumber())
				.phoneNumber(teacherRegisterationRequest.getPhoneNumber())
				.qualification(teacherRegisterationRequest.getQualification())
				.subjects(teacherRegisterationRequest.getSubjects()).address(teacherRegisterationRequest.getAddress())
				.build();
		teacherRepository.save(teacher);
		return ResponseEntity.status(HttpStatus.CREATED).body(new GlobalTeacherResponse(
				"Teacher is registered successfully " + " email id--", HttpStatus.CREATED.value()));
	}
}