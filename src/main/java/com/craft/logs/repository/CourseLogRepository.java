package com.craft.logs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.craft.logs.repository.entity.CourseLogs;
@Repository
public interface CourseLogRepository extends MongoRepository<CourseLogs, Integer>{
}

