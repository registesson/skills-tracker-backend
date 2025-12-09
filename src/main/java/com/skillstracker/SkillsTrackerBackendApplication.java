package com.skillstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class SkillsTrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillsTrackerBackendApplication.class, args);
	}

}
