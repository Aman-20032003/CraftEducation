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
import com.craft.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;

	@GetMapping("/login")
	public ResponseEntity<AdminResponse> login(@RequestBody AdminLoginRequest adminLoginRequest) {
		  AdminResponse response = adminService.adminLogin(adminLoginRequest);
	        if (response.isSuccess()) {
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	}
}
