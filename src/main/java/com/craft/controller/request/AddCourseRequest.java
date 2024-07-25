package com.craft.controller.request;

import java.util.List;

import com.craft.repository.entity.Subject;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseRequest {
	String courseName;
	String coureDetails;
	String courseDuration;
	double courseFee;
	List<AddSubjectRequest> subjects;
}
