package com.supplychain.tenant_service.service;

import com.supplychain.tenant_service.Exception.DuplicateEntityException;
import com.supplychain.tenant_service.Exception.UserNotFoundException;
import com.supplychain.tenant_service.model.Dto.UserDTO;
import com.supplychain.tenant_service.model.Entity.Tenant;
import com.supplychain.tenant_service.model.Entity.User;
import com.supplychain.tenant_service.model.eNums.UserRole;
import com.supplychain.tenant_service.Repository.TenantRepository;
import com.supplychain.tenant_service.Repository.UserRepository;
import com.supplychain.tenant_service.Security.TenantContext;
import com.supplychain.tenant_service.service.event.TenantEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final TenantEventPublisher eventPublisher;
    
    @Transactional
    public UserDTO createUser(UUID tenantId, String email, String password,String fullName, UserRole role) {
        
        // Validate tenant exists
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        
        // Check if user already exists in this tenant
        if (userRepository.existsByEmailAndTenant_Id(email, tenantId)) {
            throw new DuplicateEntityException("User with email already exists in this tenant: " + email);
        }
        
        // Create user
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(role)
                .tenant(tenant)
                .isActive(true)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("Created user: {} for tenant: {}", email, tenantId);
        
        // Publish event
        eventPublisher.publishUserRegistered(
                tenantId,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
        
        return mapToDTO(savedUser);
    }
    
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Ensure user belongs to current tenant
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (currentTenantId != null && !user.getTenant().getId().equals(currentTenantId)) {
            throw new SecurityException("Cannot access user from another tenant");
        }
        
        return mapToDTO(user);
    }
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        
        // Ensure user belongs to current tenant
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (currentTenantId != null && !user.getTenant().getId().equals(currentTenantId)) {
            throw new SecurityException("Cannot access user from another tenant");
        }
        
        return mapToDTO(user);
    }
    
    public List<UserDTO> getUsersByTenant(UUID tenantId) {
        // Ensure request is for current tenant
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (!tenantId.equals(currentTenantId)) {
            throw new SecurityException("Cannot access users from another tenant");
        }
        
        return userRepository.findByTenant_Id(tenantId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public UserDTO updateUser(UUID userId, String fullName, UserRole role, Boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Ensure user belongs to current tenant
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (!user.getTenant().getId().equals(currentTenantId)) {
            throw new SecurityException("Cannot update user from another tenant");
        }
        
        if (fullName != null) {
            user.setFullName(fullName);
        }
        
        if (role != null) {
            user.setRole(role);
        }
        
        if (isActive != null) {
            user.setIsActive(isActive);
        }
        
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }
    
    @Transactional
    public void updateLastLogin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Ensure user belongs to current tenant
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (!user.getTenant().getId().equals(currentTenantId)) {
            throw new SecurityException("Cannot delete user from another tenant");
        }
        
        userRepository.delete(user);
        log.info("Deleted user: {}", userId);
    }
    
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .tenantId(user.getTenant().getId())
                .isActive(user.getIsActive())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
