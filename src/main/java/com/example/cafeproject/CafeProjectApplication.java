package com.example.cafeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeProjectApplication.class, args);
	}

}
