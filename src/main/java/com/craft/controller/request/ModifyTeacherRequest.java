package com.craft.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyTeacherRequest {

	private String name;
	private long aadharNumber;
	private long phoneNumber;
	private String qualification;
	private double salary;

}
