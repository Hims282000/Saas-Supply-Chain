package com.supplychain.tenant_service.base;


import com.supplychain.tenant.components.testcontainers.DatabaseContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    
    @BeforeAll
    static void setup() {
        DatabaseContainer.start();
    }
    
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DatabaseContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", DatabaseContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", DatabaseContainer.getInstance()::getPassword);
        
        // Kafka properties (if using Kafka in tests)
        registry.add("spring.kafka.bootstrap-servers", 
                () -> "localhost:9092"); // Update with actual Kafka container
    }
}