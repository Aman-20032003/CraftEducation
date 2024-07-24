package com.craft.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeacherLoginRequest {
	private String email;
	private String password;
}
