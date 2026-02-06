package com.supplychain.tenant.components.builders;

import com.supplychain.tenant.model.entity.Tenant;
import com.supplychain.tenant.model.entity.User;
import com.supplychain.tenant.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserBuilder {
    
    private UUID id;
    private String email;
    private String password;
    private String fullName;
    private UserRole role = UserRole.VIEWER;
    private Tenant tenant;
    private Boolean isActive = true;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public UserBuilder withId(UUID id) {
        this.id = id;
        return this;
    }
    
    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }
    
    public UserBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
    
    public UserBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }
    
    public UserBuilder withTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }
    
    public UserBuilder withIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }
    
    public UserBuilder withLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
        return this;
    }
    
    public User build() {
        if (tenant == null) {
            tenant = TenantBuilder.defaultTenant();
        }
        
        User user = User.builder()
                .id(id)
                .email(email)
                .password(password)
                .fullName(fullName)
                .role(role)
                .tenant(tenant)
                .isActive(isActive)
                .lastLoginAt(lastLoginAt)
                .createdAt(createdAt)
                .build();
        
        return user;
    }
    
    public static UserBuilder aUser() {
        return new UserBuilder();
    }
    
    public static User defaultUser() {
        return aUser()
                .withId(UUID.randomUUID())
                .withEmail("user@test.com")
                .withPassword("encodedPassword")
                .withFullName("Test User")
                .withRole(UserRole.VIEWER)
                .build();
    }
    
    public static User adminUser() {
        return aUser()
                .withId(UUID.randomUUID())
                .withEmail("admin@test.com")
                .withPassword("encodedPassword")
                .withFullName("Admin User")
                .withRole(UserRole.ADMIN)
                .build();
    }
}