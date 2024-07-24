package com.craft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.craft.repository.entity.Admin;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	@Bean
	UserDetailsService detailsService() {
		UserDetails user = (UserDetails) Student.builder().email("ak@gmail.com").password(encoder().encode("abc")).role("Student")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();

	}
	@SuppressWarnings("deprecation")
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest()
			      .permitAll())
			      .csrf(AbstractHttpConfigurer::disable);
			    return http.build();
            
	}
}
