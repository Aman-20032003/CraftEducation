package com.craft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.response.CourseResponse;
import com.craft.controller.response.ShowCourseResponse;
import com.craft.repository.entity.Course;
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
	
	@GetMapping("/getcourses")
	public ResponseEntity<List<ShowCourseResponse>> showCourseDetails (){
		return  ResponseEntity.ok(icourseService.showCourses());
		
	}
	
} 
