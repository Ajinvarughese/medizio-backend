package com.project.medizio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedizioApplication {
	public static void main(String[] args) {
		SpringApplication.run(MedizioApplication.class, args);
	}
}
