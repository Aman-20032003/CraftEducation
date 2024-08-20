package com.craft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.RemoveTeacherRequest;
import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.request.TeacherRegisterationRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.controller.response.TeacherResponse;
import com.craft.service.TeacherServiceImp;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

@Autowired
TeacherServiceImp teacherService;


@PostMapping("/registeration")

public ResponseEntity<TeacherResponse> teacherRegisteration(@RequestBody TeacherRegisterationRequest registerationRequest){
	return teacherService.registerNewTeacher(registerationRequest);
	
}
@PostMapping("/login")
public ResponseEntity<JwtResponse> teacherLogin (@RequestBody TeacherLoginRequest loginRequest){
	return teacherService.teacherLogin(loginRequest);

}
@DeleteMapping("/removeTeacher")
public ResponseEntity<TeacherResponse>removeTeacher(@RequestBody RemoveTeacherRequest removeTeacherRequest){
	return teacherService.removeTeacher(removeTeacherRequest);
}
}
