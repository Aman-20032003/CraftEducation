package com.craft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.craft.repository.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer>{

	

}
