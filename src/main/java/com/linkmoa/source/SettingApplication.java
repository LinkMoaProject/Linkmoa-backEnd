package com.linkmoa.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettingApplication.class, args);
	}

}
