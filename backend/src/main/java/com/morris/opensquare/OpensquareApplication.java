package com.morris.opensquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OpensquareApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(OpensquareApplication.class, args);
	}
}
