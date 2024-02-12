package com.morris.opensquare;

import com.morris.opensquare.services.AtlasFunctionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OpensquareApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(OpensquareApplication.class, args);
		AtlasFunctionService template = applicationContext.getBean(AtlasFunctionService.class);

		// test kotlin setup in project
		template.func();
	}

}
