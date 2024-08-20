package com.craft.service;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.controller.response.JwtResponse;

public interface IAdminService {
	public ResponseEntity<JwtResponse> adminLogin(AdminLoginRequest adminLoginRequest) ;

}
