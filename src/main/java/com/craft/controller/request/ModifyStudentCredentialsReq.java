package com.craft.controller.request;

import lombok.Data;

@Data
public class ModifyStudentCredentialsReq {
	private String name;
	private String fatherName;
	private String email;
	private String motherName;
	private long aadharCardNo;
	private String highQualification;
	private long contactNo;

}
