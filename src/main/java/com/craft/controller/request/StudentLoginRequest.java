package com.craft.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentLoginRequest {
	
private String email;
private String password;

}
