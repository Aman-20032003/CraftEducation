package com.craft.logs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.craft.logs.repository.entity.TeacherLogs;
@Repository
public interface TeacherLogRepository extends MongoRepository<TeacherLogs, Integer> {
}
