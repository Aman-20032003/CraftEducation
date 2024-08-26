
package com.craft.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.craft.config.JwtHelper;
import com.craft.controller.request.RemoveTeacherRequest;
import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.JwtRepository;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.TeachersAddress;
import com.craft.repository.entity.JwtToken;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Teacher;
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
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtRepository jwtRepository;

//	TEACHER REGISTERATION SERVICE
	public ResponseEntity<TeacherResponse> registerNewTeacher(TeacherRegisterationRequest teacherRegisterationRequest) {
		List<TeachersAddress> addresses = addressConverter
				.convertAddressListToEntity(teacherRegisterationRequest.getAddress());
		List<TeachersSubject> subjects = teachersSubjectEntityConverter
				.convertStremOfTeachersSubjectListToEntity(teacherRegisterationRequest.getSubjects());
		Pattern pattern = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(teacherRegisterationRequest.getEmailId());
		if (!matcher.matches()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("Invalid email syntax", HttpStatus.BAD_REQUEST.value()));
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
				.password(encoder.encode(teacherRegisterationRequest.getPassword())).salary(teacherRegisterationRequest.getSalary())
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

		Teacher teacher = teacherRepository.findByEmailIdAndPassword(email, password);
		if (teacher != null) {
			UserDetails details = customUserDetailsService.loadUserByUsername(teacherLoginRequest.getEmail());
			String token = helper.generateToken(details);
			String existingtoken = getOrGenerateToken(teacher.getEmailId());
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

	private String getOrGenerateToken(String userEmail) {
		JwtToken existingToken = jwtRepository.findByEmail(userEmail);

		if (existingToken != null) {
			Date now = new Date();
			if (existingToken.getExpiresAt().after(now)) {
				// Token is still valid, return it
				return existingToken.getToken();
			} else {
				// Token is expired, remove it
				jwtRepository.delete(existingToken);
			}
		}

		// Token does not exist or is expired, generate a new one
		UserDetails details = customUserDetailsService.loadUserByUsername(userEmail);
		String newToken = helper.generateToken(details);
		saveJwtToken(userEmail, newToken);
		return newToken;
	}

	private void saveJwtToken(String userEmail, String token) {
		Date issuedAt = new Date();
		Date expiresAt = new Date(issuedAt.getTime() + JwtHelper.JWT_TOKEN_VALIDITY * 1000);
		JwtToken jwtToken = JwtToken.builder().email(userEmail).issuedAt(issuedAt).token(token).expiresAt(expiresAt)
				.build();
		jwtRepository.save(jwtToken); // Save the token in the database
	}

	public ResponseEntity<TeacherResponse> removeTeacher(RemoveTeacherRequest removeTeacherRequest) {
		Teacher teacher = teacherRepository.findByEmailId(removeTeacherRequest.getEmail());
		if (teacher != null) {
			teacherRepository.delete(teacher);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new TeacherResponse("Teacher Removed Successfully", HttpStatus.OK.value()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new TeacherResponse("Teacher Not Found", HttpStatus.NOT_FOUND.value()));
	}
}
