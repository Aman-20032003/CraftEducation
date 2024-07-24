package com.craft.repository.entity;

import java.util.List;

import com.craft.controller.request.AddSubjectRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	String courseName;
	String courseDetails;
	@OneToMany(cascade = CascadeType.PERSIST)
	List<Subject> subject;
}
