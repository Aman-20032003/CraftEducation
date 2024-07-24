package com.craft.repository.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class TeachersSubject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@JsonIgnoreProperties("subjects")
	private int id;
	private String subjectName; 
	
//	@ManyToOne
//	@JsonIgnore
//	private Teacher teacher;
}
