
package com.craft.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.craft.config.JwtHelper;
import com.craft.controller.request.RemoveTeacherRequest;
import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.TeachersAddress;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Teacher;
import com.craft.repository.entity.TeachersSubject;
import com.craft.service.helper.DtoToAddressEntityConverter;
import com.craft.service.helper.DtoToTeachersSubjectEntityConverter;

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
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	LogService logService;
	
	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtHelper helper;

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
				.password(teacherRegisterationRequest.getPassword()).salary(teacherRegisterationRequest.getSalary())
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

		String cacheValue = "cacheTeacher";

		Teacher teacher = (Teacher) redisTemplate.opsForHash().get(email, cacheValue);
		if (teacher != null) {
			UserDetails details = customUserDetailsService.loadUserByUsername(teacherLoginRequest.getEmail());
			String token = helper.generateToken(details);

			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(
					"Teacher loggedin successfully " + " email id--" + teacherLoginRequest.getEmail(), true, token));
		}

		Teacher teacher2 = teacherRepository.findByEmailIdAndPassword(email, password);
		if (teacher2 != null) {
			UserDetails details = customUserDetailsService.loadUserByUsername(teacherLoginRequest.getEmail());
			String token = helper.generateToken(details);

			redisTemplate.opsForHash().put(email, cacheValue, teacher2);
			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(
					"Teacher loggedin successfully " + " email id--" + teacherLoginRequest.getEmail(), true, token));

		} else {

			evictCache(email);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse(
					" login request failed" + " email id--" + teacherLoginRequest.getEmail(), false, null));
		}

	}

	public void evictCache(String email) {
		redisTemplate.delete(email);
	}
 public ResponseEntity<TeacherResponse>removeTeacher(RemoveTeacherRequest removeTeacherRequest){
	 Teacher teacher= teacherRepository.findByEmailId(removeTeacherRequest.getEmail());
	 if(teacher!=null) {
		 return ResponseEntity.status(HttpStatus.OK).body(new TeacherResponse("Teacher Removed Successfully", HttpStatus.OK.value()));
	 }
	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TeacherResponse("Teacher Not Found", HttpStatus.NOT_FOUND.value()));
 }
}
