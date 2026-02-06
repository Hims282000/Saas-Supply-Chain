package com.supplychain.tenant_service.Model.DTO;


import com.supplychain.tenant_service.model.eNums.SubscriptionTier;
import com.supplychain.tenant_service.model.Dto.AuthRequest;
import com.supplychain.tenant_service.model.Dto.AuthResponse;
import com.supplychain.tenant_service.model.Dto.RegisterRequest;
import com.supplychain.tenant_service.model.Dto.TenantDTO;
import com.supplychain.tenant_service.model.Dto.UserDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidAuthRequest() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("valid.email@example.com");
        authRequest.setPassword("ValidPass123!");
        
        // When
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        
        // Then
        assertTrue(violations.isEmpty(), "Valid AuthRequest should have no violations");
    }

    @Test
    void testAuthRequestWithInvalidEmail() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("invalid-email");
        authRequest.setPassword("ValidPass123!");
        
        // When
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
        assertTrue(violations.iterator().next().getMessage().contains("email"));
    }

    @Test
    void testAuthRequestWithEmptyEmail() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("");
        authRequest.setPassword("ValidPass123!");
        
        // When
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testAuthRequestWithNullEmail() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(null);
        authRequest.setPassword("ValidPass123!");
        
        // When
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testAuthRequestWithEmptyPassword() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("valid@example.com");
        authRequest.setPassword("");
        
        // When
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("password", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testValidRegisterRequest() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setTenantName("Valid Company");
        registerRequest.setSubdomain("valid-company");
        registerRequest.setSubscriptionTier(SubscriptionTier.STARTER);
        registerRequest.setEmail("admin@validcompany.com");
        registerRequest.setPassword("SecurePass123!");
        registerRequest.setFullName("Admin User");
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        
        // Then
        assertTrue(violations.isEmpty(), "Valid RegisterRequest should have no violations");
    }

    @Test
    void testRegisterRequestWithInvalidEmail() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setTenantName("Test Company");
        registerRequest.setSubdomain("test");
        registerRequest.setSubscriptionTier(SubscriptionTier.FREE);
        registerRequest.setEmail("invalid-email");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testRegisterRequestWithEmptyTenantName() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setTenantName("");
        registerRequest.setSubdomain("test");
        registerRequest.setSubscriptionTier(SubscriptionTier.FREE);
        registerRequest.setEmail("valid@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("tenantName", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testRegisterRequestWithNullSubscriptionTier() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setTenantName("Test Company");
        registerRequest.setSubdomain("test");
        registerRequest.setSubscriptionTier(null);
        registerRequest.setEmail("valid@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        
        // Then
        assertEquals(1, violations.size());
        assertEquals("subscriptionTier", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void testDtoBuilders() {
        // Test TenantDTO builder
        TenantDTO.TenantConfigDTO configDTO = TenantDTO.TenantConfigDTO.builder()
                .timezone("UTC")
                .currency("USD")
                .emailNotifications(true)
                .smsNotifications(false)
                .maxUsers(10)
                .maxShipmentsPerMonth(1000)
                .build();
        
        assertNotNull(configDTO);
        assertEquals("UTC", configDTO.getTimezone());
        
        // Test UserDTO builder
        UserDTO userDTO = UserDTO.builder()
                .email("test@example.com")
                .fullName("Test User")
                .isActive(true)
                .build();
        
        assertNotNull(userDTO);
        assertEquals("Test User", userDTO.getFullName());
        
        // Test AuthResponse builder
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(userDTO)
                .build();
        
        assertNotNull(authResponse);
        assertEquals("Bearer", authResponse.getTokenType());
    }
}
