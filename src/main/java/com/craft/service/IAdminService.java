package com.craft.service;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.AdminResponse;

public interface IAdminService {
	public AdminResponse adminLogin(AdminLoginRequest adminLoginRequest) ;

}
