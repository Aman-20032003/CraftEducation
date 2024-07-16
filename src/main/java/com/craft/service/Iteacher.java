package com.craft.service;

import com.craft.controller.request.TeacherLoginRequest;
import com.craft.controller.response.GlobalTeacherResponse;

public interface Iteacher {
		 public GlobalTeacherResponse teacherLogin(TeacherLoginRequest loginRequest);
}
