package com.craft.service;

import org.springframework.http.ResponseEntity;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.request.ShowCourseRequest;
import com.craft.controller.response.CourseResponse;

public interface ICourseService {
	public ResponseEntity<CourseResponse> addCourse(AddCourseRequest addCourseRequest);
	public ResponseEntity<CourseResponse> showCourses(ShowCourseRequest showCourseRequest);
}
