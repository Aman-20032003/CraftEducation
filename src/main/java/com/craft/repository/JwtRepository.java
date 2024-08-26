package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.JwtToken;

public interface JwtRepository extends JpaRepository<JwtToken, Integer> {
	JwtToken findByEmail(String email);
	JwtToken findByToken(String token);
}
