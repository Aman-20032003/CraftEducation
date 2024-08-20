package com.craft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.controller.response.JwtResponse;
import com.craft.service.AdminServiceImp;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminServiceImp adminService;

	@GetMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AdminLoginRequest adminLoginRequest) {
		 return adminService.adminLogin(adminLoginRequest);
	}
}
