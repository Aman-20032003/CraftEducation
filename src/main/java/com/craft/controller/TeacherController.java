package com.craft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.TeacherResponse;
import com.craft.service.TeacherService;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

@Autowired
TeacherService teacherService;


@PostMapping("/registeration")

public ResponseEntity<TeacherResponse> teacherRegisteration(@RequestBody TeacherRegisterationRequest registerationRequest){
	return teacherService.registerNewTeacher(registerationRequest);
	
}
@PostMapping("/login")
public ResponseEntity<TeacherResponse> teacherLogin (@RequestBody TeacherLoginRequest loginRequest){
	return teacherService.teacherLogin(loginRequest);

}
}
