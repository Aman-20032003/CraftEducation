package com.craft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.response.CourseResponse;
import com.craft.service.ICourseService;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private ICourseService icourseService;
	
	@PostMapping("/add")
	public  ResponseEntity<CourseResponse> addNewCourse(@RequestBody AddCourseRequest addCourseRequest){
		return icourseService.addCourse(addCourseRequest);
	}
}
