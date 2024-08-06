package com.craft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.craft.repository.StudentRepository;
import com.craft.repository.entity.Student;
@Service
public class CustomUserDetailsService implements UserDetailsService {
@Autowired
private StudentRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
	 Student student=repository.findByEmail(username);
		return student;
	}

}
