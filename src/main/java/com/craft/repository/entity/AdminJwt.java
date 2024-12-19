package com.craft.repository.entity;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class AdminJwt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int jwt_Id;
	private String email;
	@Column(nullable = false, unique = true, length = 1024)
	private String token;
	private Date issuedAt;
	private Date expiresAt;
    @OneToOne
    @JoinColumn(name = "id")
    private Admin admin;

}

