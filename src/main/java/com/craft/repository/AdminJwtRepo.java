package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.AdminJwt;

public interface AdminJwtRepo extends JpaRepository<AdminJwt, Integer> {
	AdminJwt findByEmail(String email);

}
