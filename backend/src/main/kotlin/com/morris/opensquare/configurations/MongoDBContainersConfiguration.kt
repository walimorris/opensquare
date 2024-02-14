package com.morris.opensquare.configurations

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container

@TestConfiguration(proxyBeanMethods = false)
class MongoDBContainersConfiguration {

    companion object {

        @Container
        @ServiceConnection
        val mongoDbContainer: MongoDBContainer = MongoDBContainer("mongo:latest")
            .apply { withExposedPorts(27017) }
            .apply { withReuse(true) }
    }
}