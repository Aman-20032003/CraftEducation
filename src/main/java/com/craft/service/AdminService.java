package com.craft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;
import com.craft.repository.AdminRepository;
import com.craft.repository.entity.Admin;

@Service
public class AdminService {

//	@Autowired
//	private AdminRepository adminRepository;

	// @Cacheable(value = "cacheAdmin", key = "#adminLoginRequest.getEmail")
//	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
//
//		Admin admin = adminRepository.findByEmailAndPassword(adminLoginRequest.getEmail(),
//				adminLoginRequest.getPassword());
//		if (admin != null) {
//			return new AdminResponse("Login Successfully", true);
//		}
//		evictCache(adminLoginRequest.getEmail());
//		return new AdminResponse("Login Failed !! Invalid Email or Password", false);
//	}
//
//	@CacheEvict(value = "cacheAdmin", key = "#email")
//	public void evictCache(String email) {
//		// The @CacheEvict annotation will take care of removing the cache entry

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private CacheManager cacheManager;

	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) {
		String email = adminLoginRequest.getEmail();
		String password = adminLoginRequest.getPassword();

		Admin admin = adminRepository.findByEmailAndPassword(email, password);
		if (admin != null) {
			cacheManager.getCache("cacheAdmin").put(email, admin);
			return new AdminResponse("Login Successfully", true);
		} else {
			evictCache(email);
			return new AdminResponse("Login Failed !! Invalid Email or Password", false);
		}
	}

	@CacheEvict(value = "cacheAdmin", key = "#email")
	public void evictCache(String email) {

	}

}
