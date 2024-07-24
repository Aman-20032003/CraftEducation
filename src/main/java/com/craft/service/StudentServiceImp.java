package com.craft.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.ModifyStudentCredentialsReq;
import com.craft.controller.request.RemoveStudentRequest;
import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.StudentResponse;
import com.craft.repository.StudentRepository;
import com.craft.repository.entity.Student;

@Service
public class StudentServiceImp implements IStudentService {

	@Autowired
	private StudentRepository repository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public ResponseEntity<StudentResponse> studentRegister(StudentRegRequest regRequest) {
		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
		Matcher m = p.matcher(regRequest.getEmail());
		if (!m.find()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Email Format Or Email Must Not Be Empty", false));
		}
		Student student1 = repository.findByEmail(regRequest.getEmail());
		if (student1 != null) {
			new StudentResponse("User Already Exists", false);
		}
		Student student = Student.builder().email(regRequest.getEmail()).password(regRequest.getPassword())
				.name(regRequest.getName()).aadharCardNo(regRequest.getAadharCardNo())
				.fatherName(regRequest.getFatherName()).motherName(regRequest.getMotherName())
				.highQualification(regRequest.getHighQualification()).contactNo(regRequest.getContactNo()).build();
		if (student != null) {
			repository.save(student);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new StudentResponse("Student Registeration Successfully", true));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new StudentResponse("Student Registeration Successfully", true));

	}

	public ResponseEntity<StudentResponse> studentLogin(StudentLoginRequest loginRequest) {
		String cacheValue = "cacheStudent";
		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
		Matcher m = p.matcher(loginRequest.getEmail());
		if (!m.find()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Email Format Or Email Must Not Be Empty", false));
		}
		Student sCache = (Student) redisTemplate.opsForHash().get(loginRequest.getEmail(), cacheValue);
		if (sCache != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Login Successfully", true));
		}
		Student student = repository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
		if (student != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Login Successfully", true));
		}
		evictCache(loginRequest.getEmail());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new StudentResponse("Login Failed !! Invalid Email or Password", false));

	}

	public void evictCache(String email) {
		redisTemplate.delete(email);
	}

	public ResponseEntity<StudentResponse> removeStudent(RemoveStudentRequest removeStudentRequest) {
		Student student = repository.findByEmail(removeStudentRequest.getEmail());
		if (student != null) {
			repository.delete(student);
			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Removed Successfully", true));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StudentResponse("Student Not Found", false));
	}

	@Override
	public List<Student> displayStudents() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public ResponseEntity<StudentResponse> modifyStudentCredentials(String email,ModifyStudentCredentialsReq credentialsReq) {

		Student student = repository.findByEmail(email);
		if (student != null) {
			student.setAadharCardNo(credentialsReq.getAadharCardNo());
			student.setContactNo(credentialsReq.getContactNo());
			student.setEmail(credentialsReq.getEmail());
			student.setFatherName(credentialsReq.getFatherName());
			student.setHighQualification(credentialsReq.getHighQualification());
			student.setMotherName(credentialsReq.getMotherName());
			student.setName(credentialsReq.getName());
			repository.save(student);
			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Credentials Updated Successfully",true));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StudentResponse("Student Not Found With Email : "+email,false));
	}

}
