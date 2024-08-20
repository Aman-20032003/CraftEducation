package com.craft.controller.response;

import java.util.List;

import com.craft.repository.entity.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowCourseResponse {
	String courseName;
	String courseDescription;
	String duration;
	double fee;
	List<ShowSubjectResponse> subject;
}
