package com.example.reviewSystem;

import com.example.service.UserService;
import org.hibernate.service.spi.InjectService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.controller","com.example.service","com.example.config","com.example.interceptor"})
@EntityScan("com.example.models")
@EnableJpaRepositories("com.example.repository")
public class ReviewSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewSystemApplication.class, args);
	}

}
