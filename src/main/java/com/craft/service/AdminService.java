package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.repository.AdminRepository;
import com.craft.repository.entity.Admin;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Cacheable(value = "cacheAdmin", key = "#adminLoginRequest != null && #adminLoginRequest.getLoginId() != null ? #adminLoginRequest.getLoginId() : 'defaultKey'")
	public ResponseEntity<AdminResponse> adminLogin(AdminLoginRequest adminLoginRequest) {

		Admin admin = adminRepository.findByLoginIdAndPassword(adminLoginRequest.getLoginId(),
				adminLoginRequest.getPassword());
		if (admin != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new AdminResponse("Login Successfully", true));
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new AdminResponse("Login Failed !! Invalid Id or Password", false));
	}
}
