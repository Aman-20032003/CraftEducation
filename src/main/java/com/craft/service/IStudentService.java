package com.craft.service;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.StudentResponse;

public interface IStudentService {
	public ResponseEntity<StudentResponse> StudentRegister(StudentRegRequest regRequest);
	public ResponseEntity<StudentResponse> StudentLogin(StudentLoginRequest loginRequest);
	
}
