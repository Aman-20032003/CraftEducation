package com.craft.controller.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentRegRequest {
	private String email;
	private String password;
	private String name;
	private long aadharCardNo;
	private String qualification;
	private long contactNo;
	private List<StudentCourseRequest> courses;
	private List<AddressRequest> address;
	
}
