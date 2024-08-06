
package com.craft.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.TeachersAddress;
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

//	TEACHER REGISTERATION SERVICE
	public ResponseEntity<TeacherResponse> registerNewTeacher(TeacherRegisterationRequest teacherRegisterationRequest) {
		List<TeachersAddress> addresses = addressConverter.convertAddressListToEntity(teacherRegisterationRequest.getAddress());
		List<TeachersSubject> subjects  = teachersSubjectEntityConverter.convertStremOfTeachersSubjectListToEntity(teacherRegisterationRequest.getSubjects());
		Pattern pattern = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-zA-Z]{2,}$");
		Matcher matcher = pattern.matcher(teacherRegisterationRequest.getEmailId());
		if (!matcher.matches()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TeacherResponse("invalid email syntax ", HttpStatus.BAD_REQUEST.value()));
		}

		Teacher getTeacher = teacherRepository.findByEmailId(teacherRegisterationRequest.getEmailId());
		if (getTeacher != null) {
			log.warn(logService.logDetailsOfTeacher("the teacher is already registered", LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new TeacherResponse(
							"teacher already exists" + " email id-- " + teacherRegisterationRequest.getEmailId(),
							HttpStatus.CONFLICT.value()));

		}
//		List<TeachersSubject> teachersSubject = teacherRegisterationRequest.getSubjects().stream().collect(Collectors.toList());
		
		
		Teacher teacher = Teacher.builder().name(teacherRegisterationRequest.getName())
				.emailId(teacherRegisterationRequest.getEmailId())
				.aadharNumber(teacherRegisterationRequest.getAadharNumber())
				.password(teacherRegisterationRequest.getPassword())
				.salary(teacherRegisterationRequest.getSalary())
				.phoneNumber(teacherRegisterationRequest.getPhoneNumber())
				.qualification(teacherRegisterationRequest.getQualification())
				.subjects(subjects)
				.address(addresses).build();
		
		teacherRepository.save(teacher);
		log.info(logService.logDetailsOfTeacher("teacher registered successfully", LogLevels.INFO));
		return ResponseEntity.status(HttpStatus.CREATED).body(new TeacherResponse(
				"Teacher is registered successfully " + " email id--" + teacherRegisterationRequest.getEmailId(),
				HttpStatus.CREATED.value()));
	}
// TEACHER LOGIN SERVICE
	public ResponseEntity<TeacherResponse> teacherLogin(TeacherLoginRequest  teacherLoginRequest) {
		
			String email = teacherLoginRequest.getEmail();
			String password= teacherLoginRequest.getPassword();
			
			String cacheValue="cacheTeacher";
			
			Teacher teacher = (Teacher) redisTemplate.opsForHash().get(email,cacheValue);
			if (teacher != null) {
				return ResponseEntity.status(HttpStatus.OK).body( new TeacherResponse("Teacher loggedin successfully " + " email id--" +teacherLoginRequest.getEmail(),
				HttpStatus.OK.value()));
			}

			Teacher teacher2 = teacherRepository.findByEmailIdAndPassword(email, password);
			if (teacher2 != null) {
			redisTemplate.opsForHash().put(email,cacheValue,teacher2);
			return ResponseEntity.status(HttpStatus.OK).body( new TeacherResponse("Teacher loggedin successfully " + " email id--" +teacherLoginRequest.getEmail(),
					HttpStatus.OK.value()));
				
			} else {

				evictCache(email);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( new TeacherResponse(" login request failed" + " email id--" +teacherLoginRequest.getEmail(),
						HttpStatus.UNAUTHORIZED.value()));
			}
			
	}
	public void evictCache(String email) {
	      redisTemplate.delete(email);
	}
}
