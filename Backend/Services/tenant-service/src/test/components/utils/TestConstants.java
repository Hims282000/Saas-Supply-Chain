package com.supplychain.tenant.components.utils;

import java.util.UUID;

public class TestConstants {
    
    // Test UUIDs
    public static final UUID TEST_TENANT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final UUID TEST_USER_ID = UUID.fromString("223e4567-e89b-12d3-a456-426614174001");
    public static final UUID TEST_ADMIN_ID = UUID.fromString("323e4567-e89b-12d3-a456-426614174002");
    
    // Test Data
    public static final String TEST_TENANT_NAME = "Test Company Inc.";
    public static final String TEST_TENANT_SUBDOMAIN = "test-company";
    public static final String TEST_USER_EMAIL = "user@testcompany.com";
    public static final String TEST_ADMIN_EMAIL = "admin@testcompany.com";
    public static final String TEST_PASSWORD = "TestPassword123!";
    public static final String TEST_USER_FULLNAME = "Test User";
    public static final String TEST_ADMIN_FULLNAME = "Admin User";
    
    // JWT
    public static final String TEST_JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIyMjNlNDU2Ny1lODliLTEyZDMtYTQ1Ni00MjY2MTQxNzQwMDEiLCJ0ZW5hbnRJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTYxNjIzOTAyMiwiZXhwIjoxNjE2MzI1NDIyfQ." +
            "test-signature";
    
    // API Paths
    public static final String AUTH_REGISTER_PATH = "/api/auth/register";
    public static final String AUTH_LOGIN_PATH = "/api/auth/login";
    public static final String TENANTS_BASE_PATH = "/api/tenants";
    public static final String USERS_BASE_PATH = "/api/users";
    
    private TestConstants() {
        // Utility class
    }
}