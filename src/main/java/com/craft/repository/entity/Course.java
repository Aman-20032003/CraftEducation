package com.craft.repository.entity;

import java.util.List;

import com.craft.controller.request.AddSubjectRequest;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Course{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String courseName;
	String courseDescription;
	String duration;
	double fee;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	List<Subject> subjects;
}
