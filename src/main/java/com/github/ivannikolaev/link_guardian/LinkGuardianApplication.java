package com.github.ivannikolaev.link_guardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LinkGuardianApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkGuardianApplication.class, args);
	}

}
