package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Admin findByEmailAndPassword(String loginId, String password);
	Admin findByEmail(String email);
	


}
