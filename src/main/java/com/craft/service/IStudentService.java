package com.craft.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.ModifyStudentCredentialsReq;
import com.craft.controller.request.RemoveStudentRequest;
import com.craft.controller.request.StudentLoginRequest;
import com.craft.controller.request.StudentRegRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.StudentResponse;
import com.craft.repository.entity.Student;

public interface IStudentService {
	public ResponseEntity<StudentResponse> studentRegister(StudentRegRequest regRequest);
	
	public ResponseEntity<JwtResponse> studentLogin(StudentLoginRequest loginRequest);
	
	public ResponseEntity<StudentResponse> removeStudent(RemoveStudentRequest removeStudentRequest);
	
	public List<Student> displayStudents();
	
	public ResponseEntity<StudentResponse> modifyStudentCredentials(String email,ModifyStudentCredentialsReq credentialsReq) ;		
}