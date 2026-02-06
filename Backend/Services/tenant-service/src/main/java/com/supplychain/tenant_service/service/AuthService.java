package com.supplychain.tenant_service.service;


import com.supplychain.tenant_service.Exception.DuplicateEntityException;
import com.supplychain.tenant_service.model.Dto.AuthRequest;
import com.supplychain.tenant_service.model.Dto.AuthResponse;
import com.supplychain.tenant_service.model.Dto.RegisterRequest;
import com.supplychain.tenant_service.model.Dto.TenantDTO;
import com.supplychain.tenant_service.model.Dto.UserDTO;
import com.supplychain.tenant_service.model.eNums.UserRole;
import com.supplychain.tenant_service.Security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final TenantService tenantService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Starting registration for tenant: {}", request.getTenantName());
        
        // Step 1: Create tenant
        TenantDTO tenantDTO = tenantService.createTenant(
                request.getTenantName(),
                request.getSubdomain(),
                request.getSubscriptionTier()
        );
        
        // Step 2: Create admin user for this tenant
        UserDTO userDTO = userService.createUser(
                tenantDTO.getId(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                UserRole.ADMIN
        );
        
        // Step 3: Generate JWT token
        String token = jwtTokenProvider.generateToken(
                userDTO.getId(),
                tenantDTO.getId(),
                userDTO.getEmail(),
                userDTO.getRole().name()
        );
        
        log.info("Registration completed for tenant: {} with admin: {}", request.getTenantName(), request.getEmail());
        return buildAuthResponse(token, userDTO, tenantDTO);
    }
    
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get authenticated user
            UserDTO userDTO = userService.getUserByEmail(request.getEmail());
            
            // Get tenant info
            TenantDTO tenantDTO = tenantService.getTenantById(userDTO.getTenantId());
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(
                    userDTO.getId(),
                    userDTO.getTenantId(),
                    userDTO.getEmail(),
                    userDTO.getRole().name()
            );
            
            // Update last login
            userService.updateLastLogin(userDTO.getId());
            
            log.info("User logged in: {}", request.getEmail());
            
            return buildAuthResponse(token, userDTO, tenantDTO);
            
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
    
    public AuthResponse refreshToken(String oldToken) {
        if (!jwtTokenProvider.validateToken(oldToken)) {
            throw new BadCredentialsException("Invalid token");
        }
        
        UUID userId = jwtTokenProvider.getUserIdFromToken(oldToken);
        UUID tenantId = jwtTokenProvider.getTenantIdFromToken(oldToken);
        String role = jwtTokenProvider.getRoleFromToken(oldToken);
        
        UserDTO userDTO = userService.getUserById(userId);
        TenantDTO tenantDTO = tenantService.getTenantById(tenantId);
        
        // Generate new token
        String newToken = jwtTokenProvider.generateToken(
                userDTO.getId(),
                tenantDTO.getId(),
                userDTO.getEmail(),
                userDTO.getRole().name()
        );
        
        return buildAuthResponse(newToken, userDTO, tenantDTO);
    }
    
    private AuthResponse buildAuthResponse(String token, UserDTO userDTO, TenantDTO tenantDTO) {
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 hours in milliseconds
                .user(userDTO)
                .tenant(tenantDTO)
                .build();
    }
}
