package com.craft.logs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.craft.logs.repository.entity.StudentLogs;
@Repository
public interface StudentLogRepository extends MongoRepository<StudentLogs, Integer>{

}
