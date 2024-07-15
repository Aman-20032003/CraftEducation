package com.craft.repository.entity;

import java.util.List;
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
public class Teacher {
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int id;
	private String name;
	private String emailId;
	private String password;
	private long aadharNumber;
	private long phoneNumber;
	private List<String> subjects;
	private double salary;
	@OneToMany
	private List<Address> address;
}
