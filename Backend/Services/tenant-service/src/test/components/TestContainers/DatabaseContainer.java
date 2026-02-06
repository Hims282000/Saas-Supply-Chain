package com.supplychain.tenant.components.testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DatabaseContainer {
    
    private static final String POSTGRES_IMAGE = "postgres:16-alpine";
    private static PostgreSQLContainer<?> postgreSQLContainer;
    
    private DatabaseContainer() {
        // Utility class
    }
    
    public static synchronized PostgreSQLContainer<?> getInstance() {
        if (postgreSQLContainer == null) {
            postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);
        }
        return postgreSQLContainer;
    }
    
    public static void start() {
        if (!getInstance().isRunning()) {
            getInstance().start();
        }
    }
    
    public static void stop() {
        if (postgreSQLContainer != null && postgreSQLContainer.isRunning()) {
            postgreSQLContainer.stop();
        }
    }
}