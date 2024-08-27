package com.craft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.craft.config.JwtHelper;
import com.craft.controller.request.ModifyStudentCredentialsReq;
import com.craft.controller.request.RemoveStudentRequest;
import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.StudentResponse;
import com.craft.controller.response.TeacherResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.StudentRepository;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Student;
import com.craft.repository.entity.StudentAdddress;
import com.craft.repository.entity.StudentCourse;
import com.craft.service.helper.DtoToAddressEntityConverter;
import com.craft.service.helper.DtoToStudentCourseEntityConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImp implements IStudentService {
	
	@Autowired
	DtoToStudentCourseEntityConverter entityConverter;
	@Autowired
	DtoToAddressEntityConverter addressEntityConverter;
	@Autowired
	private StudentRepository repository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private LogService logService;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private JwtHelper helper;

	public ResponseEntity<StudentResponse> studentRegister(StudentRegRequest regRequest) {
		List<StudentCourse> courses =entityConverter.convertStremOfCourseToEntity(regRequest.getStudentCourses()) ;
		List<StudentAdddress> addresses =addressEntityConverter.convertStudentAddressListToEntity(regRequest.getStudentAddress());
//		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
//		Matcher m = p.matcher(regRequest.getEmail());
//		if (!m.find()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new StudentJwtResponse("Invalid Email Format Or Email Must Not Be Empty", false, null));
//		}
		Student studentInRepository = repository.findByEmail(regRequest.getEmail());
		if (studentInRepository != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new StudentResponse("Student already exists", false));
		}
		Student student = Student.builder().email(regRequest.getEmail()).password(regRequest.getPassword())
				.name(regRequest.getName()).aadharCardNo(regRequest.getAadharCardNo())
				.qualification(regRequest.getQualification())
				.contactNo(regRequest.getContactNo())
				.addressList(addresses)
				.courseList(courses)
				.build();
		
		if (student == null) {
			log.warn(logService.logDetailsOfStudent("Student Registration Failed! With Email: " +regRequest.getEmail(),
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
		String cacheValue = "cacheStudent";
//		Pattern p = Pattern.compile("^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$");
//		Matcher m = p.matcher(loginRequest.getEmail());
//		if (!m.find()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new JwtResponse("Invalid Email Format Or Email Must Not Be Empty", false, null));
//		}
		Student sCache = (Student) redisTemplate.opsForHash().get(loginRequest.getEmail(), cacheValue);
		if (sCache != null) {
			UserDetails details = customUserDetailsService.loadUserByUsername(sCache.getEmail());
			String token = helper.generateToken(details);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new JwtResponse("Student Login Successfully", true, token));
		}
		Student student = repository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
		if (student != null) {
			UserDetails details = customUserDetailsService.loadUserByUsername(student.getEmail());
			String token = helper.generateToken(details);
			log.info(logService.logDetailsOfStudent("Login Successfully With Email: " + loginRequest.getEmail(),
					LogLevels.INFO));

			return ResponseEntity.status(HttpStatus.OK)
					.body(new JwtResponse("Student Login Successfully", true, token));
		}
		evictCache(loginRequest.getEmail());
		log.warn(
				logService.logDetailsOfStudent("Login Failed! With Email: " + loginRequest.getEmail(), LogLevels.WARN));

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new JwtResponse("Login Failed !! Invalid Email or Password", false, null));

	}

	public void evictCache(String email) {
		redisTemplate.delete(email);
	}

	public ResponseEntity<StudentResponse> removeStudent(RemoveStudentRequest removeStudentRequest) {
		Student student = repository.findByEmail(removeStudentRequest.getEmail());
		if (student != null) {
			repository.delete(student);
			log.info(logService.logDetailsOfStudent(
					"Student Removed Successfully With Email: " + removeStudentRequest.getEmail(), LogLevels.INFO));

			return ResponseEntity.status(HttpStatus.OK).body(new StudentResponse("Student Removed Successfully", true));
		}
		log.warn(logService.logDetailsOfStudent("Student Not Found! With Email: " + removeStudentRequest.getEmail(),
				LogLevels.WARN));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StudentResponse("Student Not Found", false));
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
			student.setEmail(credentialsReq.getEmail());
			student.setQualification(credentialsReq.getQualification());
			student.setName(credentialsReq.getName());
			repository.save(student);
			evictCache(email);
			log.info(logService.logDetailsOfStudent(
					"Student Credentials Updated Successfully With Email: " + credentialsReq.getEmail(),
					LogLevels.INFO));

			return ResponseEntity.status(HttpStatus.OK)
					.body(new StudentResponse("Student Credentials Updated Successfully", true));
		}
		log.warn(logService.logDetailsOfStudent("Student Not found With Email: " + credentialsReq.getEmail(),
				LogLevels.WARN));
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new StudentResponse("Student Not Found With Email : " + email, false));
	}

}
