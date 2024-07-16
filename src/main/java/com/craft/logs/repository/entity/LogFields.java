package com.craft.logs.repository.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

 enum LogLevel {
	INFO, WARN, ERROR, DEBUG
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public abstract class LogFields {
	private String message;
	
	@Enumerated(EnumType.STRING)
	private LogLevel level;

	private String dateTime;

}