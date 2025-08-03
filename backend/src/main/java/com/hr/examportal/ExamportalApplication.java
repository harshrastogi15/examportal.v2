package com.hr.examportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ExamportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamportalApplication.class, args);
	}

}
