package com.craft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.StudentResponse;
import com.craft.service.IStudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	
@Autowired
private IStudentService service;
@PostMapping("/registeration")
	
public ResponseEntity<StudentResponse>StudentRegisteration(@RequestBody StudentRegRequest regRequest){
	return service.StudentRegister(regRequest);
	
}
@GetMapping("/login")
public ResponseEntity<StudentResponse>StudentLogin (@RequestBody StudentLoginRequest loginRequest){
	return service.StudentLogin(loginRequest);

}
}
