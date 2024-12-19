package com.craft.service;

import java.util.Date;

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
import com.craft.repository.AdminJwtRepo;
import com.craft.repository.AdminRepository;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.AdminJwt;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImp implements IAdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private AdminJwtRepo adminJwtRepo;
	@Autowired
	private LogService logService;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private JwtHelper helper;

	public ResponseEntity<JwtResponse> adminLogin(AdminLoginRequest adminLoginRequest) {
		Admin admin = adminRepository.findByEmailAndPassword(adminLoginRequest.getEmail(),
				adminLoginRequest.getPassword());
		if (admin != null) {

			UserDetails details = customUserDetailsService.loadUserByUsername(adminLoginRequest.getEmail());
			String token = helper.generateToken(details, admin.getPassword());
			String existingtoken = getOrGenerateToken(admin.getEmail(), admin.getPassword());

			Claims claims1 = JwtHelper.decodeJwt(existingtoken);
			Claims claims2 = JwtHelper.decodeJwt(token);
			System.out.println(claims1);
			System.out.println(claims2);
			if (claims1.getSubject().equals(claims2.getSubject())) {
				log.info(logService.logDetailsOfStudent(
						"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));
				return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Login Successfully", true, token));
			}
		}
			log.warn(logService.logDetailsOfStudent(
					"Admin Login Failed  Invalid Email or Password with Email: " + adminLoginRequest.getEmail(),
					LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new JwtResponse("Login Failed !! Invalid Email or Password", false, null));
		
	}

	private String getOrGenerateToken(String userEmail, String password) {
		AdminJwt existingToken = adminJwtRepo.findByEmail(userEmail);

		if (existingToken != null) {
			Date now = new Date();
			if (existingToken.getExpiresAt().after(now)) {
				// Token is still valid, return it
				return existingToken.getToken();
			} else {
				// Token is expired, remove it
				adminJwtRepo.delete(existingToken);
			}
		}

		// Token does not exist or is expired, generate a new one
		UserDetails details = customUserDetailsService.loadUserByUsername(userEmail);
		String newToken = helper.generateToken(details, password);
		saveJwtToken(userEmail, newToken);
		return newToken;
	}
		private void saveJwtToken(String userEmail, String token) {
		    Date issuedAt = new Date();
		    Date expiresAt = new Date(issuedAt.getTime() + JwtHelper.JWT_TOKEN_VALIDITY * 1000);
		    Admin admin = adminRepository.findByEmail(userEmail);

		    AdminJwt existingToken = adminJwtRepo.findByEmail(userEmail);
		    if (existingToken != null) {
		        // Update existing token
		        existingToken.setToken(token);
		        existingToken.setIssuedAt(issuedAt);
		        existingToken.setExpiresAt(expiresAt);
		        adminJwtRepo.save(existingToken);
		    } else {
		        // Save a new token
		        AdminJwt jwtToken = AdminJwt.builder()
		            .email(userEmail)
		            .issuedAt(issuedAt)
		            .token(token)
		            .expiresAt(expiresAt)
		            .admin(admin)
		            .build();
		        adminJwtRepo.save(jwtToken);
		    }
		}


}
