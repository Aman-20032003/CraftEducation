package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private LogService logService;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private JwtHelper helper;

	public ResponseEntity<JwtResponse> adminLogin(AdminLoginRequest adminLoginRequest) {
		String email = adminLoginRequest.getEmail();
		String password = adminLoginRequest.getPassword();
		String cacheValue = "cacheAdmin";
		Admin admin = (Admin) redisTemplate.opsForHash().get(email, cacheValue);
		if (admin != null) {
			UserDetails detailsService = customUserDetailsService.loadUserByUsername(admin.getEmail());
			String token = helper.generateToken(detailsService);
			log.info(logService.logDetailsOfStudent(
					"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));

			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Login Successfully", true, token));
		}

		Admin admin1 = adminRepository.findByEmailAndPassword(email, password);
		if (admin1 != null) {
			UserDetails detailsService = customUserDetailsService.loadUserByUsername(admin1.getEmail());
			String token = helper.generateToken(detailsService);

			redisTemplate.opsForHash().put(email, cacheValue, admin1);
			log.info(logService.logDetailsOfStudent(
					"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));
			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Login Successfully", true, token));
		} else {
			evictCache(email);
			log.warn(logService.logDetailsOfStudent(
					"Admin Login Failed  Invalid Email or Password with Email: " + adminLoginRequest.getEmail(),
					LogLevels.WARN));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new JwtResponse("Login Failed !! Invalid Email or Password", false, null));
		}
	}

	public void evictCache(String email) {
		redisTemplate.delete(email);
	}
}
