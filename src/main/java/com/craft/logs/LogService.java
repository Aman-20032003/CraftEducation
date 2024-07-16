package com.craft.logs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import com.craft.logs.repository.entity.*;
import com.craft.logs.repository.AdminLogRepository;
import com.craft.logs.repository.StudentLogRepository;
import com.craft.logs.repository.TeacherLogRepository;
import com.craft.logs.repository.entity.AdminLogs;

@Service 
public class LogService {
	private AdminLogRepository adminLogRepository ;
	private TeacherLogRepository teacherLogRepository;
	private StudentLogRepository studentLogRepository;
	
	 @Autowired
	public LogService(AdminLogRepository adminLogRepository, TeacherLogRepository teacherLogRepository,
			StudentLogRepository studentLogRepository) {
		this.adminLogRepository = adminLogRepository;
		this.teacherLogRepository = teacherLogRepository;
		this.studentLogRepository = studentLogRepository;
	}
	
	
	LocalDateTime localDateTime = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd-MM-yyyy HH:mm:ss");
	
	
	
	
	public String logDetailsOfAdmin(String message , String logLevel) {
		String formattedDateTime = localDateTime.format(formatter);
		AdminLogs adminLogs= AdminLogs.builder()
				.message(message)
				.dateTime(formattedDateTime)
				.level(logLevel).build();
		return message;
		
	}
	
	public String logDetailsOfTeacher(String message,LogLevel logLevel) {
		String formattedDateTime = localDateTime.format(formatter);
		return message;
		
	}
	
	public String logDetailsOfStudent(String message,LogLevel logLevel) {
		String formattedDateTime = localDateTime.format(formatter);
		return message;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
