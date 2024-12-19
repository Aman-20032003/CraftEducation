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

import com.craft.controller.request.ModifyStudentCredentialsReq;
import com.craft.controller.request.RemoveStudentRequest;
import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.StudentResponse;

import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.AdminRepository;
import com.craft.repository.StudentJwtRepo;
import com.craft.repository.StudentRepository;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Student;
import com.craft.repository.entity.StudentAdddress;
import com.craft.repository.entity.StudentCourse;
import com.craft.repository.entity.StudentJWT;
import com.craft.repository.entity.Teacher;
import com.craft.service.helper.DtoToAddressEntityConverter;
import com.craft.service.helper.DtoToStudentCourseEntityConverter;

import io.jsonwebtoken.Claims;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImp implements IStudentService {

	@Autowired
	private DtoToStudentCourseEntityConverter courseEntityConverter;
	@Autowired
	private DtoToAddressEntityConverter addressEntityConverter;
	@Autowired
	private StudentRepository repository;
	@Autowired
	private LogService logService;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private JwtHelper helper;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private StudentJwtRepo studentJwtRepo;
	
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private TeacherRepository teacherRepository;

	public ResponseEntity<StudentResponse> studentRegister(StudentRegRequest regRequest) {
		List<StudentAdddress> addresses = addressEntityConverter
				.convertStudentAddressListToEntity(regRequest.getAddress());
		List<StudentCourse> courses = courseEntityConverter.convertStremOfCourseToEntity(regRequest.getCourses());
//		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
//		Matcher m = p.matcher(regRequest.getEmail());
//		if (!m.find()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new StudentJwtResponse("Invalid Email Format Or Email Must Not Be Empty", false, null));
//		}
		Admin admin= adminRepository.findByEmail(regRequest.getEmail());
		Teacher teacher= teacherRepository.findByEmailId(regRequest.getEmail());
		if(admin!=null || teacher!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new StudentResponse("Email is Already Registered As A Student Or Admin!! Try With Another Email", false));
		}
		
		Student studentInRepository = repository.findByEmail(regRequest.getEmail());

		if (studentInRepository != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new StudentResponse("Student already exists", false));
		}
		  String aadharNo = String.valueOf(regRequest.getAadharCardNo());
		if(aadharNo.length()!=12) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Aadhar No ! Must Be 12 Digits", false));
		}
		 String contactNo = String.valueOf(regRequest.getContactNo());
		if(contactNo.length()!=10) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Invalid Contact No ! Must Be 10 Digits", false));
		
		}

		Student student = Student.builder().email(regRequest.getEmail())
				.password(encoder.encode(regRequest.getPassword())).name(regRequest.getName())
				.aadharCardNo(regRequest.getAadharCardNo()).qualification(regRequest.getQualification())
				.contactNo(regRequest.getContactNo()).addressList(addresses).courseList(courses).role(Role.STUDENT)
				.build();

		if (student == null) {
			log.warn(logService.logDetailsOfStudent("Student Registration Failed! With Email: " + regRequest.getEmail(),
					LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new StudentResponse("Student Registration Failed", false));
		}
		repository.save(student);

		log.info(logService.logDetailsOfStudent("Student Registered Successfully With Email: " + student.getEmail(),
				LogLevels.INFO));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new StudentResponse("Student Registeration Successfully", true));
	}

	public ResponseEntity<JwtResponse> studentLogin(StudentLoginRequest loginRequest) {
//		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
//		Matcher m = p.matcher(loginRequest.getEmail());
//		if (!m.find()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new JwtResponse("Invalid Email Format Or Email Must Not Be Empty", false, null));
//		}

		Student student = repository.findByEmail(loginRequest.getEmail());
		if (student != null && encoder.matches(loginRequest.getPassword(), student.getPassword())) {

			UserDetails details = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
			String token = helper.generateToken(details, student.getPassword());
			String existingtoken = getOrGenerateToken(student.getEmail(), student.getPassword());

			Claims claims1 = JwtHelper.decodeJwt(existingtoken);
			Claims claims2 = JwtHelper.decodeJwt(token);
			System.out.println(claims1);
			System.out.println(claims2);
			if (claims1.getSubject().equals(claims2.getSubject())) {
				log.info(logService.logDetailsOfStudent("Login Successfully With Email: " + loginRequest.getEmail(),
						LogLevels.INFO));

				return ResponseEntity.status(HttpStatus.OK)
						.body(new JwtResponse("Student Login Successfully", true, token));
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new JwtResponse("Login Failed !! Invalid Email or Password", false, null));

	}

	private String getOrGenerateToken(String userEmail, String password) {
		StudentJWT existingToken = studentJwtRepo.findByEmail(userEmail);

		if (existingToken != null) {
			Date now = new Date();
			if (existingToken.getExpiresAt().after(now)) {
				// Token is still valid, return it
				return existingToken.getToken();
			} else {
				// Token is expired, remove it
				studentJwtRepo.delete(existingToken);
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
		Student student = repository.findByEmail(userEmail);

		StudentJWT jwtToken = StudentJWT.builder().email(userEmail).issuedAt(issuedAt).token(token).expiresAt(expiresAt)
				.student(student).build();
		studentJwtRepo.save(jwtToken); // Save the token in the database
	}

	public ResponseEntity<StudentResponse> removeStudent(RemoveStudentRequest removeStudentRequest) {
	    // Fetch the student by email
	    Student student = repository.findByEmail(removeStudentRequest.getEmail());

	    // If student is null, log a warning and return a "not found" response
	    if (student == null) {
	        log.warn(logService.logDetailsOfStudent(
	                "Student Not Found! With Email: " + removeStudentRequest.getEmail(),
	                LogLevels.WARN));
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new StudentResponse("Student Not Found", false));
	    }

	    // Fetch the associated JWT (if any)
//	    StudentJWT studentJWT = studentJwtRepo.findByEmail(student.getEmail());
//
//	    // Remove the JWT if it exists
//	    if (studentJWT != null) {
//	        studentJwtRepo.delete(studentJWT);
//	    }

	    // Remove the student
	    repository.delete(student);

	    // Log the successful removal
	    log.info(logService.logDetailsOfStudent(
	            "Student Removed Successfully With Email: " + removeStudentRequest.getEmail(),
	            LogLevels.INFO));

	    // Return a success response
	    return ResponseEntity.status(HttpStatus.OK)
	            .body(new StudentResponse("Student Removed Successfully", true));
	}


	@Override
	public List<Student> displayStudents() {

		return repository.findAll();
	}

	public ResponseEntity<StudentResponse> modifyStudentCredentials(String email,
			ModifyStudentCredentialsReq credentialsReq) {

		Student student = repository.findByEmail(email);
		if (student != null) {

			student.setAadharCardNo(credentialsReq.getAadharCardNo());
			student.setContactNo(credentialsReq.getContactNo());
			student.setQualification(credentialsReq.getQualification());
			student.setName(credentialsReq.getName());
			repository.save(student);
			log.info(logService.logDetailsOfStudent(
					"Student Credentials Updated Successfully With Email: " + student.getEmail(),
					LogLevels.INFO));

			return ResponseEntity.status(HttpStatus.OK)
					.body(new StudentResponse("Student Credentials Updated Successfully", true));
		}
		log.warn(logService.logDetailsOfStudent("Student Not found With Email: " + email,
				LogLevels.WARN));
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new StudentResponse("Student Not Found With Email : " + email, false));
	}

}
