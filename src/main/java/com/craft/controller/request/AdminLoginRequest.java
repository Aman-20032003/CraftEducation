package com.craft.controller.request;

import lombok.Data;

@Data
public class AdminLoginRequest {
	private String loginId;
	private String password;
}
