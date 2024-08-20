package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	Student findByEmailAndPassword(String email, String password);

	Student findByEmail(String email);
}
