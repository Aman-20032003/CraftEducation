package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.craft.config.JwtHelper;
import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.logs.LogService;
import com.craft.logs.repository.entity.LogLevels;
import com.craft.repository.AdminRepository;
import com.craft.repository.entity.Admin;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImp implements IAdminService {

	@Autowired
	private AdminRepository adminRepository;



	@Autowired
	private LogService logService;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private JwtHelper helper;

	public ResponseEntity<JwtResponse> adminLogin(AdminLoginRequest adminLoginRequest) {
		Admin admin= adminRepository.findByEmailAndPassword(adminLoginRequest.getEmail(), adminLoginRequest.getPassword());
		if (admin != null) {
			UserDetails detailsService = customUserDetailsService.loadUserByUsername(admin.getEmail());
			String token = helper.generateToken(detailsService);

			
			log.info(logService.logDetailsOfStudent(
					"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));
			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Login Successfully", true, token));
		} else {
			log.warn(logService.logDetailsOfStudent(
					"Admin Login Failed  Invalid Email or Password with Email: " + adminLoginRequest.getEmail(),
					LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new JwtResponse("Login Failed !! Invalid Email or Password", false, null));
		}
	}

}
