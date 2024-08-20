package com.craft;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CraftEducationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CraftEducationApplication.class, args);
	}

}