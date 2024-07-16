package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;

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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

//	@Autowired
//	private AdminRepository adminRepository;

<<<<<<< HEAD
//	@Cacheable(value = "cacheAdmin", key = "#adminLoginRequest.getEmail")
//	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
//
//		Admin admin = adminRepository.findByEmailAndPassword(adminLoginRequest.getEmail(),
//				adminLoginRequest.getPassword());
//		if (admin != null) {
//			return new AdminResponse("Login Successfully", true);
//		}
//		evictCache(adminLoginRequest.getEmail());
//		return new AdminResponse("Login Failed !! Invalid Email or Password", false);
//	}
//
//	@CacheEvict(value = "cacheAdmin", key = "#email")
//	public void evictCache(String email) {
//		// The @CacheEvict annotation will take care of removing the cache entry
	
@Autowired
private AdminRepository adminRepository;

@Autowired
private CacheManager cacheManager;

public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
    String email = adminLoginRequest.getEmail();
    String password = adminLoginRequest.getPassword();

    Admin admin = adminRepository.findByEmailAndPassword(email, password);
    if (admin != null) {
        cacheManager.getCache("cacheAdmin").put(email, admin);
        return new AdminResponse("Login Successfully", true);
    } else {
        evictCache(email);
        return new AdminResponse("Login Failed !! Invalid Email or Password", false);
    }
}

@CacheEvict(value = "cacheAdmin", key = "#email")
public void evictCache(String email) {

}

}
=======
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
>>>>>>> fa7f9984f73471d03d6bb8c66501ac38386b96a8
