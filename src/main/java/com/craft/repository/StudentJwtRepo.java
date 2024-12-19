package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.StudentJWT;

public interface StudentJwtRepo  extends JpaRepository<StudentJWT, Integer>{
 StudentJWT findByEmail(String email);	


}
