package com.craft.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.response.CourseResponse;
import com.craft.controller.response.ShowCourseResponse;

public interface ICourseService {
	public ResponseEntity<CourseResponse> addCourse(AddCourseRequest addCourseRequest);
	
	public List<ShowCourseResponse> showCourses();
}
