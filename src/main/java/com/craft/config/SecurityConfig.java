package com.craft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfig  {
	
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
	        http.csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(requests -> requests
	                .requestMatchers("/student/login", "/student/registeration").permitAll()
	                .anyRequest().authenticated())
	            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationConfig))
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	}
	   @Bean
	   DaoAuthenticationProvider authenticationProvider() {
		  DaoAuthenticationProvider authenticationProvider=  new DaoAuthenticationProvider();
		  authenticationProvider.setUserDetailsService(customUserDetailsService);
		  authenticationProvider.setPasswordEncoder(passwordEncoder);
		  return authenticationProvider;
	   }
	
  }
