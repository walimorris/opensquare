package com.morris.opensquare;

import com.morris.opensquare.services.AtlasFunctionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpensquareApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpensquareApplication.class, args);

		// test kotlin setup in project
		new AtlasFunctionService().sayHello();
	}

}
