package com.oneplusone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OneplusoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneplusoneApplication.class, args);
	}

}
