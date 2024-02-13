package com.morris.opensquare;

import com.morris.opensquare.services.WatchStreamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OpensquareApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(OpensquareApplication.class, args);
		WatchStreamService watchStreamService = applicationContext.getBean(WatchStreamService.class);

		/*
		 * Watch streams is a good way to monitor for Change Data Capture (CDC). In MongoDB, you
		 * can monitor on database or collections level. If we monitor on a database level, it
		 * only tells us about CDC on that level, and we'd need to add additional logic to break
		 * down any actions accordingly. Our watch streams monitor on a collections level, which
		 * gives us granular level of insight into what data has changed. From the WatchStreamService,
		 * we can monitor on collection level.
		 */
		watchStreamService.watchEmailDomainAdditionStream();
	}

}
