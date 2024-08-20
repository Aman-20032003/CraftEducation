package com.craft.controller.request;

import lombok.Data;

@Data
public class ModifyStudentCredentialsReq {
	private String name;	
	private String email;
	private long aadharCardNo;
	private String Qualification;
	private long contactNo;

}
