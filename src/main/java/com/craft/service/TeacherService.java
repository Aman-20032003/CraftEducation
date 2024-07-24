package com.craft.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.RemoveTeacherRequest;
import com.craft.controller.request.TeacherRegisterationRequest;

import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.Address;
import com.craft.repository.entity.Teacher;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherService {
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	DtoToEntityAddressConverterService addressConverter;

	@Autowired
	LogService logService;

//	TEACHER REGISTERATION SERVICE
	public ResponseEntity<TeacherResponse> registerNewTeacher(TeacherRegisterationRequest teacherRegisterationRequest) {
		List<Address> addresses = addressConverter.convertAddressListToEntity(teacherRegisterationRequest.getAddress());
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
		Teacher teacher = Teacher.builder().name(teacherRegisterationRequest.getName())
				.emailId(teacherRegisterationRequest.getEmailId())
				.aadharNumber(teacherRegisterationRequest.getAadharNumber())
				.phoneNumber(teacherRegisterationRequest.getPhoneNumber())
				.qualification(teacherRegisterationRequest.getQualification())
				.subjects(teacherRegisterationRequest.getSubjects()).address(addresses).build();
		teacherRepository.save(teacher);
		log.info(logService.logDetailsOfTeacher("teacher registered successfully", LogLevels.INFO));
		return ResponseEntity.status(HttpStatus.CREATED).body(new TeacherResponse(
				"Teacher is registered successfully " + " email id--" + teacherRegisterationRequest.getEmailId(),
				HttpStatus.CREATED.value()));
	}

	public ResponseEntity<TeacherResponse> RemoveTeacher(RemoveTeacherRequest removeTeacherRequest) {
		Teacher teacher = teacherRepository.findByEmailId(removeTeacherRequest.getEmail());
		if (teacher != null) {
			teacherRepository.delete(teacher);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new TeacherResponse("Teacher Removed Successfully", HttpStatus.OK.value()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new TeacherResponse("Teacher Not Found ", HttpStatus.NOT_FOUND.value()));

	}

}
