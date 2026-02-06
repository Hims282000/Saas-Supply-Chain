package com.supplychain.tenant_service.Model.DTO;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.supplychain.tenant_service.Model.DTO.AuthRequest;

class AuthRequestTest {

    @Test
    void testAuthRequestSettersAndGetters() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        
        // When
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");
        
        // Then
        assertEquals("test@example.com", authRequest.getEmail());
        assertEquals("password123", authRequest.getPassword());
    }

    @Test
    void testAuthRequestEqualsAndHashCode() {
        // Given
        AuthRequest request1 = new AuthRequest();
        request1.setEmail("test@example.com");
        request1.setPassword("password");
        
        AuthRequest request2 = new AuthRequest();
        request2.setEmail("test@example.com");
        request2.setPassword("password");
        
        AuthRequest request3 = new AuthRequest();
        request3.setEmail("different@example.com");
        request3.setPassword("password");
        
        // Then
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testAuthRequestToString() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");
        
        // When
        String toString = authRequest.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("test@example.com"));
        // Password should not be in toString for security
        assertFalse(toString.contains("password"));
    }

    @Test
    void testAuthRequestWithNullValues() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        
        // When & Then
        assertNull(authRequest.getEmail());
        assertNull(authRequest.getPassword());
    }
}