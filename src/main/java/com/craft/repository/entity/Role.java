package com.craft.repository.entity;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

public enum Role {
	ADMIN(Set.of(Permission.ADMIN_READ, Permission.ADMIN_UPDATE, Permission.ADMIN_DELETE, Permission.ADMIN_CREATE,
			Permission.STUDENT_READ, Permission.STUDENT_UPDATE, Permission.STUDENT_DELETE, Permission.STUDENT_CREATE)),
	STUDENT(Set.of(Permission.STUDENT_READ, Permission.STUDENT_UPDATE, Permission.STUDENT_DELETE,
			Permission.STUDENT_CREATE)),
	TEACHER(Set.of(Permission.TEACHER_CREATE, Permission.TEACHER_UPDATE));
	
	@Getter
	private final Set<Permission> permissions;

	Role(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return authorities;
	}

}

  enum Permission {
	ADMIN_READ("admin:read"), ADMIN_UPDATE("admin:update"), ADMIN_DELETE("admin:delete"), ADMIN_CREATE("admin:create"),
	STUDENT_READ("student:read"), STUDENT_UPDATE("student:update"), STUDENT_DELETE("student:delete"),
	 STUDENT_CREATE("student: create"),TEACHER_CREATE("teacher:create"),TEACHER_UPDATE("teacher:update");

	private final String permission;

	Permission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
}
