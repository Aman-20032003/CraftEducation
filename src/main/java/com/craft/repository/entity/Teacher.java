package com.craft.repository.entity;



import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Teacher implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private int id;
	private String name;
	private String emailId;
	private String password;
	private long aadharNumber;
	private long phoneNumber;
	private String qualification;
	@OneToMany(cascade = CascadeType.PERSIST)
//	@JsonIgnore
	private List<TeachersSubject> subjects;
	private double salary;
//	@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<TeachersAddress> address;
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return role.getAuthorities();}

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
