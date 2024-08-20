package com.craft.logs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import com.craft.logs.repository.entity.*;
import com.craft.logs.repository.AdminLogRepository;
import com.craft.logs.repository.CourseLogRepository;
import com.craft.logs.repository.StudentLogRepository;
import com.craft.logs.repository.TeacherLogRepository;
import com.craft.logs.repository.entity.AdminLogs;

@Service 
public class LogService {
	private AdminLogRepository adminLogRepository ;
	private TeacherLogRepository teacherLogRepository;
	private StudentLogRepository studentLogRepository;
	private CourseLogRepository courseLogRepository;
	
	 
	public LogService(AdminLogRepository adminLogRepository, TeacherLogRepository teacherLogRepository,
			StudentLogRepository studentLogRepository, CourseLogRepository courseLogRepository) {
		this.adminLogRepository = adminLogRepository;
		this.teacherLogRepository = teacherLogRepository;
		this.studentLogRepository = studentLogRepository;
		this.courseLogRepository = courseLogRepository;
	}

	
	
	LocalDateTime localDateTime = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd-MM-yyyy HH:mm:ss");
	String formattedDateTime = localDateTime.format(formatter);

	
	
	
	public String logDetailsOfAdmin(String message ,LogLevels logLevel) {
		AdminLogs adminLogs= AdminLogs.builder()
				.message(message)
				.dateTime(formattedDateTime)
				.level(logLevel).build();
		adminLogRepository.save(adminLogs);
		return message;
		
	}
	
	public String logDetailsOfTeacher(String message,LogLevels logLevel) {
	 TeacherLogs teacherLogs= TeacherLogs.builder()
				.message(message)
				.dateTime(formattedDateTime)
				.level(logLevel).build();
	 teacherLogRepository.save(teacherLogs);
		return message;
		
	}
	
	public String logDetailsOfStudent(String message,LogLevels logLevel) {
		StudentLogs studentLogs= StudentLogs.builder()
				.message(message)
				.dateTime(formattedDateTime)
				.level(logLevel).build();
		studentLogRepository.save(studentLogs);
		return message;
		}
	
	public String logDetailsOfCourse(String message,LogLevels logLevel) {
		CourseLogs courseLogs= CourseLogs.builder()
				.message(message)
				.dateTime(formattedDateTime)
				.level(logLevel).build();
		 courseLogRepository.save(courseLogs);
		return message;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
