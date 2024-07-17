package com.craft.controller.request;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherRegisterationRequest {
	
	private String name;
	private String emailId;
	private long aadharNumber;
	private long phoneNumber;
	private String qualification;
	private List<String> subjects;
	private double salary;
	private List<GlobalAddressRequest> address;
}
