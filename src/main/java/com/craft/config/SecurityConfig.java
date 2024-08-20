package com.craft.config;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.craft.repository.entity.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationConfig authenticationConfig;
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests

				.requestMatchers("/student/registeration", "student/login","/admin/login","/teacher/registeration","/teacher/login","/v3/api-docs", "/configuration/ui",
						"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**",
						"/swagger-ui/**")
				.permitAll()
				.requestMatchers("/student/removeStudent","/teacher/removeTeacher").hasAnyRole(Role.ADMIN.name()).
                 requestMatchers( "/student/getAll").hasAnyRole(Role.ADMIN.name(),Role.TEACHER.name()).
                 requestMatchers("/student/update/{email}").hasAnyRole(Role.STUDENT.name()).
                 
				anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationConfig))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

}
