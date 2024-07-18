package com.craft.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.controller.response.GlobalTeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.AdminRepository;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.Address;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.Teacher;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

//	@Autowired
//	private AdminRepository adminRepository;

<<<<<<< HEAD
//	@Cacheable(value = "cacheAdmin", key = "#adminLoginRequest.getEmail")
=======
	// @Cacheable(value = "cacheAdmin", key = "#adminLoginRequest.getEmail")
>>>>>>> 3376df29abfcae61e414d5c00d74eb97ae87eed2
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

<<<<<<< HEAD
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
>>>>>>> 3376df29abfcae61e414d5c00d74eb97ae87eed2
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	DtoToEntityAddressConverterService addressConverter;

	@Autowired
	LogService logService;

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

//	TEACHER REGISTERATION
	public ResponseEntity<GlobalTeacherResponse> registerNewTeacher(
			TeacherRegisterationRequest teacherRegisterationRequest) {
		List<Address> addresses = addressConverter.convertAddressListToEntity(teacherRegisterationRequest.getAddress());
		Pattern pattern = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(teacherRegisterationRequest.getEmailId());
		if (!matcher.matches()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalTeacherResponse("invalid email syntax ", HttpStatus.BAD_REQUEST.value()));
		}

		Teacher getTeacher = teacherRepository.findByEmailId(teacherRegisterationRequest.getEmailId());
		if (getTeacher != null) {
			log.warn(logService.logDetailsOfTeacher("the teacher is already registered", LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new GlobalTeacherResponse(
					"teacher already exists" + " email id-- " + teacherRegisterationRequest.getEmailId(),
					HttpStatus.CONFLICT.value()));

		}
		Teacher teacher = Teacher.builder().name(teacherRegisterationRequest.getName())
				.emailId(teacherRegisterationRequest.getEmailId())
				.aadharNumber(teacherRegisterationRequest.getAadharNumber())
				.phoneNumber(teacherRegisterationRequest.getPhoneNumber())
				.qualification(teacherRegisterationRequest.getQualification())
				.subjects(teacherRegisterationRequest.getSubjects()).address(addresses).build();
		teacherRepository.save(teacher);
		log.info(logService.logDetailsOfTeacher("teacher registered successfully", LogLevels.INFO));
		return ResponseEntity.status(HttpStatus.CREATED).body(new GlobalTeacherResponse(
				"Teacher is registered successfully " + " email id--" + teacherRegisterationRequest.getEmailId(),
				HttpStatus.CREATED.value()));
	}
}
