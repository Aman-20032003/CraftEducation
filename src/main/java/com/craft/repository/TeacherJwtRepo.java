package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.TeacherJwt;

public interface TeacherJwtRepo  extends JpaRepository<TeacherJwt, Integer>{
	TeacherJwt findByEmail(String email);

}
