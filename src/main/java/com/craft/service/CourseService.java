package com.craft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.request.ShowCourseRequest;
import com.craft.controller.response.CourseResponse;
import com.craft.repository.CourseRepository;
import com.craft.repository.entity.Course;
import com.craft.repository.entity.Subject;
import com.craft.service.helper.DtoToSubjectEntityConverter;

@Service
public class CourseService implements ICourseService {
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	DtoToSubjectEntityConverter subjectEntityConverter;

	@Override
	public ResponseEntity<CourseResponse> addCourse(AddCourseRequest addCourseRequest) {
		List<Subject> subjects = subjectEntityConverter.convertStremOfSubjectToEntity(addCourseRequest.getSubjects());
		Course newCourse = Course.builder().courseName(addCourseRequest.getCourseName())
				.courseDetails(addCourseRequest.getCoureDetails()).subject(subjects)
	            .build();
		courseRepository.save(newCourse);
		return ResponseEntity.status(HttpStatus.OK).body(new CourseResponse("new course added ",HttpStatus.OK.value()));
	}

	@Override
	public ResponseEntity<CourseResponse> showCourses(ShowCourseRequest showCourseRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
