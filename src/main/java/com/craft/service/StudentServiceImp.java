package com.craft.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.StudentResponse;
import com.craft.repository.StudentRepository;
import com.craft.repository.entity.Student;

@Service
public class StudentServiceImp implements IStudentService {

	@Autowired
	private StudentRepository repository;

	public ResponseEntity<StudentResponse> StudentRegister(StudentRegRequest regRequest) {
		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
		Matcher m = p.matcher(regRequest.getEmail());
		if (!m.find()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Email Format Or Email Must Not Be Empty", false));
		}
		Student student = Student.builder().email(regRequest.getEmail()).password(regRequest.getPassword())
				.name(regRequest.getName()).aadharCardNo(regRequest.getAadharCardNo())
				.fatherName(regRequest.getFatherName()).motherName(regRequest.getMotherName())
				.highQualification(regRequest.getHighQualification()).build();
		if (student != null) {
			repository.save(student);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new StudentResponse("Student Registeration Successfully", true));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new StudentResponse("Student Registeration Successfully", true));

	}

	public ResponseEntity<StudentResponse> StudentLogin(StudentLoginRequest loginRequest) {
		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
		Matcher m = p.matcher(loginRequest.getEmail());
		if (!m.find()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Email Format Or Email Must Not Be Empty", false));
		}
		Student student = repository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
		if (student != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Login Successfully", true));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new StudentResponse("Login Failed !! Invalid Email or Password", false));

	}
}
