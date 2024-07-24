package com.craft.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class TeachersAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@JsonIgnoreProperties("address")
	private int id;
	private String houseNumber;
	private String city;
	private int pinCode;
	private String State;
	private String country;

}
