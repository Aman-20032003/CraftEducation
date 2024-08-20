package com.craft.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.craft.controller.request.AddCourseRequest;
import com.craft.controller.response.CourseResponse;
import com.craft.controller.response.ShowCourseResponse;
import com.craft.controller.response.ShowSubjectResponse;
import com.craft.repository.CourseRepository;
import com.craft.repository.SubjectRepository;
import com.craft.repository.entity.Course;
import com.craft.repository.entity.Subject;
import com.craft.service.helper.DtoToSubjectEntityConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService implements ICourseService {
	@Autowired
	SubjectRepository subjectRepository ;
	
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	DtoToSubjectEntityConverter subjectEntityConverter;

	
//	Add Course Service
	@Override
	public ResponseEntity<CourseResponse> addCourse(AddCourseRequest addCourseRequest) {
		List<Subject> subjects = subjectEntityConverter.convertStremOfSubjectToEntity(addCourseRequest.getSubjects());
		Course newCourse = Course.builder().courseName(addCourseRequest.getCourseName())
				.duration(addCourseRequest.getCourseDuration()).fee(addCourseRequest.getCourseFee())
				.courseDescription(addCourseRequest.getCoureDetails()).subjects(subjects).build();
		courseRepository.save(newCourse);
		for (Subject subject : subjects) {	
			subjectRepository.save(subject);
		}
	    redisTemplate.opsForValue().set("allCourses", newCourse);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new CourseResponse("new course added ", HttpStatus.OK.value()));
	} 

//	Get Course Service 
	@Override 
//	@Cacheable(value = "Course")
	public List<ShowCourseResponse> showCourses() {		
		List<ShowCourseResponse> cachedCourses = (List<ShowCourseResponse>) redisTemplate.opsForValue().get("allCourses");
		    if (cachedCourses != null) {
		        return cachedCourses;
		    }
		    // Fetch data from the database if not found in Redis
		    List<Course> courses = courseRepository.findAll();
		    List<ShowCourseResponse> customResponse = new ArrayList<>();
		    for (Course course : courses) {
		        System.out.println("Course: " + course.getCourseName() + ", Subjects count: " + course.getSubjects().size());

		    List<ShowSubjectResponse> subjectResponse = course.getSubjects().stream()
		                 .map(subject -> {
		                    ShowSubjectResponse  response = ShowSubjectResponse.builder().subjectName(subject.getSubjectName())
		                    		.about(subject.getAbout()).build();
		                      return response;
		                 })
		                 .collect(Collectors.toList());
		    	
			ShowCourseResponse	 response =  ShowCourseResponse.builder().courseName(course.getCourseName())
		            .courseDescription(course.getCourseDescription()).duration(course.getDuration()).fee(course.getFee()).subject(subjectResponse).build();
		    
				customResponse.add(response);
				   
		    }
		    // Store data in redis
		    redisTemplate.opsForValue().set("allCourses", customResponse);
		    return customResponse;
	}
}
