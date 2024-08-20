 package com.craft.controller.request;

import java.util.List;

import com.craft.repository.entity.TeachersAddress;
import com.craft.repository.entity.Subject;
import com.craft.repository.entity.TeachersSubject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
public class TeacherRegisterationRequest {
	
	private String name;
	private String emailId;
	private String password;
	private long aadharNumber;
	private long phoneNumber;
	private String qualification;
	private double salary;
	private List<TeachersSubjectRequest> subjects;
	private List<AddressRequest> address;
}
