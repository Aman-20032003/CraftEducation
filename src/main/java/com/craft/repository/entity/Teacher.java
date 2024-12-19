package com.craft.repository.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotEmpty(message = "Name Must Not Be Empty")
	private String name;
	@Email(message = "Invalid Email Format")
	private String emailId;
	@JsonIgnore
	@NotEmpty(message = "Passsword Must Not BE Empty")
	private String password;
	private long aadharNumber;
	private long phoneNumber;
	@NotEmpty(message = "Qualification Must Not Be Empty")
	private String qualification;
	@OneToMany(cascade = CascadeType.ALL)
//	@JsonIgnore
	private List<TeachersSubject> subjects;
	private double salary;
//	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<TeachersAddress> address;

	private Role role;
	@JsonIgnore
	@OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL)
	private TeacherJwt teacherJwt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return emailId;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

}
