package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.repository.AdminRepository;
import com.craft.repository.entity.Admin;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

	
	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
		String email = adminLoginRequest.getEmail();
		String password= adminLoginRequest.getPassword();
		String cacheValue="cacheAdmin";
		Admin admin = (Admin) redisTemplate.opsForHash().get(email,cacheValue);
		if (admin != null) {
			return new AdminResponse("Login Successfully", true);
		}

		Admin admin1 = adminRepository.findByEmailAndPassword(email, password);
		if (admin1 != null) {
		redisTemplate.opsForHash().put(email,cacheValue,admin1);

			return new AdminResponse("Login Successfully", true);
		} else {
			evictCache(email);
			return new AdminResponse("Login Failed !! Invalid Email or Password", false);
		}
	}

	
	public void evictCache(String email) {
	      redisTemplate.delete(email);
	}
}
