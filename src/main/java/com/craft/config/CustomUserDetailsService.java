package com.craft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.craft.repository.AdminRepository;
import com.craft.repository.StudentRepository;
import com.craft.repository.TeacherRepository;
import com.craft.repository.entity.Admin;
import com.craft.repository.entity.Role;
import com.craft.repository.entity.Student;
import com.craft.repository.entity.Teacher;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private StudentRepository repository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

			Student student = repository.findByEmail(username);
			if (student != null) {
				return student;
			}
			Teacher teacher = teacherRepository.findByEmailId(username);
			if (teacher != null) {
				return teacher;
			}
			Admin admin = adminRepository.findByEmail(username);
			if (admin != null) {
				return admin;
			}
		// If the user is not found in any repository, throw an exception
		throw new UsernameNotFoundException("User not found with username: " + username);

	}

	
}
