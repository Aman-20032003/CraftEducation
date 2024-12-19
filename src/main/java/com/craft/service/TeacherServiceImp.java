
package com.craft.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import com.craft.config.JwtHelper;
import com.craft.controller.request.ModifyTeacherRequest;
import com.craft.controller.request.RemoveTeacherRequest;
import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.JwtResponse;

import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.AdminRepository;
import com.craft.repository.StudentRepository;
import com.craft.repository.TeacherJwtRepo;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.TeachersAddress;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Student;
import com.craft.repository.entity.Teacher;
import com.craft.repository.entity.TeacherJwt;
import com.craft.repository.entity.TeachersSubject;
import com.craft.service.helper.DtoToAddressEntityConverter;
import com.craft.service.helper.DtoToTeachersSubjectEntityConverter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherServiceImp implements ITeacherService {
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	DtoToAddressEntityConverter addressConverter;

	@Autowired
	DtoToTeachersSubjectEntityConverter teachersSubjectEntityConverter;

	@Autowired
	LogService logService;

	@Autowired
	private UserDetailsService customUserDetailsService;

	@Autowired
	private JwtHelper helper;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private TeacherJwtRepo teacherJwtRepo;
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private StudentRepository studentRepository;

//	TEACHER REGISTERATION SERVICE
	public ResponseEntity<TeacherResponse> registerNewTeacher(TeacherRegisterationRequest teacherRegisterationRequest) {
		List<TeachersAddress> addresses = addressConverter
				.convertAddressListToEntity(teacherRegisterationRequest.getAddress());
		List<TeachersSubject> subjects = teachersSubjectEntityConverter
				.convertStremOfTeachersSubjectListToEntity(teacherRegisterationRequest.getSubjects());
//		Pattern pattern = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-zA-Z]{2,}$");
//		Matcher matcher = pattern.matcher(teacherRegisterationRequest.getEmailId());
//		if (!matcher.matches()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new TeacherResponse("Invalid email syntax", HttpStatus.BAD_REQUEST.value()));
//		}
		Admin admin = adminRepository.findByEmail(teacherRegisterationRequest.getEmailId());

		Student student = studentRepository.findByEmail(teacherRegisterationRequest.getEmailId());
		if (admin != null || student != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new TeacherResponse(
							"Email is Already Registered As A Student Or Admin!! Try With Another Email ",
							HttpStatus.CONFLICT.value()));
		}
		String aadharNo = String.valueOf(teacherRegisterationRequest.getAadharNumber());
		if (aadharNo.length() != 12) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Invalid Aadhar No ! Must Be 12 Digits", HttpStatus.BAD_REQUEST.value()));
		}
		String contactNo = String.valueOf(teacherRegisterationRequest.getPhoneNumber());
		if (contactNo.length() != 10) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new TeacherResponse("Invalid Contact No ! Must Be 10 Digits", HttpStatus.BAD_REQUEST.value()));

		}

		if (teacherRegisterationRequest.getSalary() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Salary must be a positive value.", HttpStatus.BAD_REQUEST.value()));
		}

		Teacher getTeacher = teacherRepository.findByEmailId(teacherRegisterationRequest.getEmailId());
		if (getTeacher != null) {
			log.warn(logService.logDetailsOfTeacher("The teacher is already registered", LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new TeacherResponse(
							"Teacher already exists with email id: " + teacherRegisterationRequest.getEmailId(),
							HttpStatus.CONFLICT.value()));
		}

		Teacher teacher = Teacher.builder().name(teacherRegisterationRequest.getName())
				.emailId(teacherRegisterationRequest.getEmailId())
				.aadharNumber(teacherRegisterationRequest.getAadharNumber())
				.password(encoder.encode(teacherRegisterationRequest.getPassword()))
				.salary(teacherRegisterationRequest.getSalary())
				.phoneNumber(teacherRegisterationRequest.getPhoneNumber())
				.qualification(teacherRegisterationRequest.getQualification()).subjects(subjects).address(addresses)
				.role(Role.TEACHER).build();

		teacherRepository.save(teacher);
		log.info(logService.logDetailsOfTeacher("Teacher registered successfully", LogLevels.INFO));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new TeacherResponse(
						"Teacher is registered successfully with email id: " + teacherRegisterationRequest.getEmailId(),
						HttpStatus.CREATED.value()));
	}

// TEACHER LOGIN SERVICE
	public ResponseEntity<JwtResponse> teacherLogin(TeacherLoginRequest teacherLoginRequest) {

		String email = teacherLoginRequest.getEmail();
		String password = teacherLoginRequest.getPassword();

		Teacher teacher = teacherRepository.findByEmailId(email);
		if (teacher != null && encoder.matches(password, teacher.getPassword())) {
			UserDetails details = customUserDetailsService.loadUserByUsername(teacherLoginRequest.getEmail());
			String token = helper.generateToken(details, teacher.getPassword());
			String existingtoken = getOrGenerateToken(teacher.getEmailId(), teacher.getPassword());
			Claims claims1 = JwtHelper.decodeJwt(existingtoken);
			Claims claims2 = JwtHelper.decodeJwt(token);
			if (claims1.getSubject().equals(claims2.getSubject())) {

				return ResponseEntity.status(HttpStatus.OK)
						.body(new JwtResponse(
								"Teacher loggedin successfully " + " email id--" + teacherLoginRequest.getEmail(), true,
								token));
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				new JwtResponse(" login request failed" + " email id--" + teacherLoginRequest.getEmail(), false, null));
	}

	private String getOrGenerateToken(String userEmail, String password) {
		TeacherJwt existingToken = teacherJwtRepo.findByEmail(userEmail);

		if (existingToken != null) {
			Date now = new Date();
			if (existingToken.getExpiresAt().after(now)) {
				// Token is still valid, return it
				return existingToken.getToken();
			} else {
				// Token is expired, remove it
				teacherJwtRepo.delete(existingToken);
			}
		}

		// Token does not exist or is expired, generate a new one
		UserDetails details = customUserDetailsService.loadUserByUsername(userEmail);
		String newToken = helper.generateToken(details, password);
		saveJwtToken(userEmail, newToken);
		return newToken;
	}

	private void saveJwtToken(String userEmail, String token) {
		Date issuedAt = new Date();
		Date expiresAt = new Date(issuedAt.getTime() + JwtHelper.JWT_TOKEN_VALIDITY * 1000);
		Teacher teacher = teacherRepository.findByEmailId(userEmail);
		TeacherJwt jwtToken = TeacherJwt.builder().email(userEmail).issuedAt(issuedAt).token(token).expiresAt(expiresAt)
				.teacher(teacher).build();
		teacherJwtRepo.save(jwtToken); // Save the token in the database
	}

	public ResponseEntity<TeacherResponse> removeTeacher(RemoveTeacherRequest removeTeacherRequest) {
		// Fetch the teacher by email
		Teacher teacher = teacherRepository.findByEmailId(removeTeacherRequest.getEmail());

		// If teacher is null, return a "not found" response
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new TeacherResponse("Teacher Not Found", HttpStatus.NOT_FOUND.value()));
		}

//		// Fetch the corresponding JWT if it exists
//		TeacherJwt teacherJwt = teacherJwtRepo.findByEmail(teacher.getEmailId());
//
//		// Remove the JWT if it exists
//		if (teacherJwt != null) {
//			teacherJwtRepo.delete(teacherJwt);
//		}

		// Remove the teacher
		teacherRepository.delete(teacher);

		// Return a successful response
		return ResponseEntity.status(HttpStatus.OK)
				.body(new TeacherResponse("Teacher Removed Successfully", HttpStatus.OK.value()));
	}

	public ResponseEntity<TeacherResponse> updateTeacher(String email, ModifyTeacherRequest modifyTeacherRequest) {
		Teacher teacher = teacherRepository.findByEmailId(email);
		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Teacher Not Found With Email " + email, HttpStatus.BAD_REQUEST.value()));
		}
		String aadharNo = String.valueOf(modifyTeacherRequest.getAadharNumber());
		if (aadharNo.length() != 12) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Invalid Aadhar No ! Must Be 12 Digits", HttpStatus.BAD_REQUEST.value()));
		}
		String contactNo = String.valueOf(modifyTeacherRequest.getPhoneNumber());
		if (contactNo.length() != 10) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new TeacherResponse("Invalid Contact No ! Must Be 10 Digits", HttpStatus.BAD_REQUEST.value()));

		}

		if (modifyTeacherRequest.getSalary() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Salary must be a positive value.", HttpStatus.BAD_REQUEST.value()));
		}

		teacher.setName(modifyTeacherRequest.getName());
		teacher.setPhoneNumber(modifyTeacherRequest.getPhoneNumber());
		teacher.setQualification(modifyTeacherRequest.getQualification());
		teacher.setAadharNumber(modifyTeacherRequest.getAadharNumber());
		teacher.setSalary(modifyTeacherRequest.getSalary());
		
		teacherRepository.save(teacher);
		return ResponseEntity.status(HttpStatus.OK).body(new TeacherResponse(
				"Teacher Credentials Updated Successfully with email " + email, HttpStatus.OK.value()));

	}
}
