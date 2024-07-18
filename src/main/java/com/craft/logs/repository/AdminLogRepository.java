package com.craft.logs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.craft.logs.repository.entity.AdminLogs;
@Repository
public interface AdminLogRepository extends MongoRepository<AdminLogs, Integer>{
}
