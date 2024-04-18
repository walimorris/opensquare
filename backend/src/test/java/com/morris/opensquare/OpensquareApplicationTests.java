package com.morris.opensquare;

import com.morris.opensquare.configurations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class OpensquareApplicationTests {

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Here we can test that the Opensquare application contexts and required Beans are available and
	 * ready to fire.
	 */
	@Test
	void contextLoads() {

		ApplicationPropertiesConfiguration appConfigs = applicationContext.getBean(ApplicationPropertiesConfiguration.class);
		HttpSessionConfiguration sessionConfiguration = applicationContext.getBean(HttpSessionConfiguration.class);
		JacksonConfiguration jacksonConfiguration = applicationContext.getBean(JacksonConfiguration.class);
		KafkaProducerConfiguration kafkaProducerConfiguration = applicationContext.getBean(KafkaProducerConfiguration.class);
		SpringMongoConfig springMongoConfig = applicationContext.getBean(SpringMongoConfig.class);

		assertAll(
				() -> Assertions.assertNotNull(applicationContext, "Application Context ready to fire."),
				() -> Assertions.assertNotNull(appConfigs, "Application Configurations ready to fire."),
				() -> Assertions.assertNotNull(sessionConfiguration, "Session Configurations ready to fire."),
				() -> Assertions.assertNotNull(jacksonConfiguration, "Jackson Configurations read to fire."),
				() -> Assertions.assertNotNull(kafkaProducerConfiguration, "Kafka Configurations ready to fire."),
				() -> Assertions.assertNotNull(springMongoConfig, "SpringBoot/MongoDB Configurations ready to fire.")
		);
	}
}
