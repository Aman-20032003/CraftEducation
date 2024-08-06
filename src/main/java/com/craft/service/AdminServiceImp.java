package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
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

	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
		String email = adminLoginRequest.getEmail();
		String password = adminLoginRequest.getPassword();
		String cacheValue = "cacheAdmin";
		Admin admin = (Admin) redisTemplate.opsForHash().get(email, cacheValue);
		if (admin != null) {
			log.info(logService.logDetailsOfStudent(
					"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));

			return new AdminResponse("Login Successfully", true);
		}

		Admin admin1 = adminRepository.findByEmailAndPassword(email, password);
		if (admin1 != null) {
			redisTemplate.opsForHash().put(email, cacheValue, admin1);
			log.info(logService.logDetailsOfStudent(
					"Admin Login Successfully With Email: " + adminLoginRequest.getEmail(), LogLevels.INFO));
			return new AdminResponse("Login Successfully", true);
		} else {
			evictCache(email);
			log.warn(logService.logDetailsOfStudent(
					"Admin Login Failed  Invalid Email or Password with Email: " + adminLoginRequest.getEmail(),
					LogLevels.WARN));
			return new AdminResponse("Login Failed !! Invalid Email or Password", false);
		}
	}

	public void evictCache(String email) {
		redisTemplate.delete(email);
	}
}
