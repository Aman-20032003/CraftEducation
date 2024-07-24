package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craft.repository.entity.Teacher;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
 public Teacher findByEmailId(String string);

public Teacher findByEmailIdAndPassword(String email, String password);
}
