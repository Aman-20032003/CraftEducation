package com.craft.repository.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Teacher  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int id;
	private String name;
	private String emailId;
	private String password;
	private long aadharNumber;
	private long phoneNumber;
	private String qualification;
	@OneToMany(cascade = CascadeType.PERSIST)
//	@JsonIgnore
	private List<TeachersSubject> subjects;
	private double salary;
//	@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<TeachersAddress> address;
}
