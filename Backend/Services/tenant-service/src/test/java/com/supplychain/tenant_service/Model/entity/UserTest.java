package com.supplychain.tenant_service.Model.entity;


import com.supplychain.tenant_service.model.eNums.UserRole;
import com.supplychain.tenant_service.model.Entity.Tenant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Tenant tenant;
    private UUID userId;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        tenantId = UUID.randomUUID();
        
        tenant = Tenant.builder()
                .id(tenantId)
                .name("Test Company")
                .subdomain("test")
                .build();
        
        user = User.builder()
                .id(userId)
                .email("john.doe@example.com")
                .password("encryptedPassword123")
                .fullName("John Doe")
                .role(UserRole.ADMIN)
                .tenant(tenant)
                .isActive(true)
                .lastLoginAt(LocalDateTime.now().minusDays(1))
                .createdAt(LocalDateTime.now().minusDays(30))
                .build();
    }

    @Test
    void testUserCreation() {
        // Then
        assertEquals(userId, user.getId());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("encryptedPassword123", user.getPassword());
        assertEquals("John Doe", user.getFullName());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertEquals(tenant, user.getTenant());
        assertTrue(user.getIsActive());
        assertNotNull(user.getLastLoginAt());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testUserDetailsImplementation() {
        // When
        String username = user.getUsername();
        String password = user.getPassword();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        boolean isAccountNonExpired = user.isAccountNonExpired();
        boolean isAccountNonLocked = user.isAccountNonLocked();
        boolean isCredentialsNonExpired = user.isCredentialsNonExpired();
        boolean isEnabled = user.isEnabled();
        
        // Then
        assertEquals("john.doe@example.com", username);
        assertEquals("encryptedPassword123", password);
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.iterator().next().getAuthority().contains("ADMIN"));
        assertTrue(isAccountNonExpired);
        assertTrue(isAccountNonLocked);
        assertTrue(isCredentialsNonExpired);
        assertTrue(isEnabled);
    }

    @Test
    void testInactiveUserDetails() {
        // Given
        User inactiveUser = User.builder()
                .email("inactive@example.com")
                .password("password")
                .fullName("Inactive User")
                .role(UserRole.VIEWER)
                .tenant(tenant)
                .isActive(false)
                .build();
        
        // When & Then
        assertFalse(inactiveUser.isAccountNonLocked());
        assertFalse(inactiveUser.isEnabled());
        assertTrue(inactiveUser.isAccountNonExpired());
        assertTrue(inactiveUser.isCredentialsNonExpired());
    }

    @Test
    void testDifferentRoles() {
        // Test ADMIN role
        user.setRole(UserRole.ADMIN);
        assertTrue(user.getAuthorities().iterator().next().getAuthority().contains("ADMIN"));
        
        // Test MANAGER role
        user.setRole(UserRole.MANAGER);
        assertTrue(user.getAuthorities().iterator().next().getAuthority().contains("MANAGER"));
        
        // Test VIEWER role
        user.setRole(UserRole.VIEWER);
        assertTrue(user.getAuthorities().iterator().next().getAuthority().contains("VIEWER"));
        
        // Test ANALYST role
        user.setRole(UserRole.ANALYST);
        assertTrue(user.getAuthorities().iterator().next().getAuthority().contains("ANALYST"));
        
        // Test SUPER_ADMIN role
        user.setRole(UserRole.SUPER_ADMIN);
        assertTrue(user.getAuthorities().iterator().next().getAuthority().contains("SUPER_ADMIN"));
    }

    @Test
    void testUserWithoutTenant() {
        // Given
        User userWithoutTenant = User.builder()
                .email("notenant@example.com")
                .password("password")
                .fullName("No Tenant")
                .role(UserRole.VIEWER)
                .build();
        
        // Then
        assertNull(userWithoutTenant.getTenant());
    }

    @Test
    void testUserEqualityById() {
        // Given
        User sameIdUser = User.builder()
                .id(userId)
                .email("different@example.com")
                .password("different")
                .fullName("Different Name")
                .tenant(tenant)
                .build();
        
        User differentIdUser = User.builder()
                .id(UUID.randomUUID())
                .email("john.doe@example.com")
                .password("encryptedPassword123")
                .fullName("John Doe")
                .tenant(tenant)
                .build();
        
        // Then
        assertEquals(user.getId(), sameIdUser.getId());
        assertNotEquals(user.getId(), differentIdUser.getId());
    }

    @Test
    void testUserToString() {
        // When
        String toString = user.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("john.doe@example.com"));
        assertTrue(toString.contains("John Doe"));
    }

    @Test
    void testUserBuilderDefaultValues() {
        // Given
        User defaultUser = User.builder()
                .email("default@example.com")
                .password("password")
                .fullName("Default User")
                .tenant(tenant)
                .build();
        
        // Then
        assertEquals(UserRole.VIEWER, defaultUser.getRole());
        assertTrue(defaultUser.getIsActive());
        assertNull(defaultUser.getLastLoginAt());
        assertNotNull(defaultUser.getCreatedAt());
    }
}