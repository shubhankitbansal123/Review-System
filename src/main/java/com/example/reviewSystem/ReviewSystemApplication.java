package com.example.reviewSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.controller", "com.example.interceptor"})
@EntityScan("com.example.models")
@EnableJpaRepositories("com.example.repository")
public class ReviewSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewSystemApplication.class, args);
	}

}
