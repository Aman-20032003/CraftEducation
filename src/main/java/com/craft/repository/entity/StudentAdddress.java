package com.craft.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentAdddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String houseNumber;
	private String city;
	private int pinCode;
	private String State;
	private String country;
}
