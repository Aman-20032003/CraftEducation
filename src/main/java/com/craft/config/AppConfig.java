package com.craft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.craft.repository.entity.Student;

@Configuration
public class AppConfig {
//	 @Bean
//	    public UserDetailsService userDetailsService() {
//	        UserDetails userDetails = Student.builder().
//	                email("aman@gmail.com")
//	                .password(passwordEncoder().encode("DURGESH")).
//	                build();
//	        return new InMemoryUserDetailsManager(userDetails);
//	    }
//
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
}
