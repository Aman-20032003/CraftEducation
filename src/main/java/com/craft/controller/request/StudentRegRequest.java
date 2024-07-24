package com.craft.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentRegRequest {
	private String email ;
	private String password; 
	private String name ;
	 private String fatherName;
	 private String motherName;
	 private long aadharCardNo;
	 private String highQualification;
	 private long contactNo;
}
