package com.craft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.ModifyStudentCredentialsReq;
import com.craft.controller.request.RemoveStudentRequest;
import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.StudentResponse;
import com.craft.repository.entity.Student;
import com.craft.service.IStudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	
@Autowired
private IStudentService service;

@PostMapping("/registeration")
public ResponseEntity<StudentResponse> StudentRegisteration(@RequestBody StudentRegRequest regRequest){
	return service.studentRegister(regRequest);
	
}
@GetMapping("/login")
public ResponseEntity<JwtResponse>StudentLogin (@RequestBody StudentLoginRequest loginRequest){
	return service.studentLogin(loginRequest);

}
//@PreAuthorize("hasRole('STUDENT')")
@DeleteMapping("/removeStudent")
public ResponseEntity<StudentResponse>deleteStudent(@RequestBody RemoveStudentRequest removeStudentRequest){
	return service.removeStudent(removeStudentRequest);
}
@GetMapping("/getAll")
public List<Student>getAllStudents(){
	return service.displayStudents();
}

@PutMapping("/update/{email}")
public ResponseEntity<StudentResponse>updateStudentCrerdentials(@PathVariable String email , @RequestBody ModifyStudentCredentialsReq credentialsReq){
	return service.modifyStudentCredentials(email, credentialsReq);
}
}
