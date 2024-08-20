package com.craft.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craft.controller.request.StudentCourseRequest;
import com.craft.repository.entity.StudentCourse;
@Service
public class DtoToStudentCourseEntityConverter {
	 public StudentCourse  convertToEntity(StudentCourseRequest courseRequest) {
		  StudentCourse course = StudentCourse.builder().courseName(courseRequest.getCourseName()).build();
		return course;
	  }
	  
	  public List<StudentCourse> convertStremOfCourseToEntity(List<StudentCourseRequest> courses ){
		  return courses.stream().map(this::convertToEntity).collect(Collectors.toList());
	  }
	
}
