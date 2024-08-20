package com.craft.service;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.TeacherResponse;

public interface ITeacherService {
	public ResponseEntity<TeacherResponse> registerNewTeacher(TeacherRegisterationRequest teacherRegisterationRequest);
	public ResponseEntity<JwtResponse> teacherLogin(TeacherLoginRequest  teacherLoginRequest);
}
