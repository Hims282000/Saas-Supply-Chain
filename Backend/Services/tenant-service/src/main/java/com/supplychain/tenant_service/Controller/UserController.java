package com.supplychain.tenant_service.Controller;


import com.supplychain.tenant_service.model.Dto.UserDTO;
import com.supplychain.tenant_service.model.eNums.UserRole;
import com.supplychain.tenant_service.Security.TenantContext;
import com.supplychain.tenant_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User management endpoints")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDTO> createUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam UserRole role) {
        
        UUID tenantId = TenantContext
        .getCurrentTenant();
        if (tenantId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        UserDTO user = userService.createUser(tenantId, email, password, fullName, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user info")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UUID tenantId = TenantContext.getCurrentTenant();
        // In a real implementation, you'd get the user ID from the JWT token
        // For now, this is a placeholder
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all users in current tenant")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByTenant() {
        UUID tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<UserDTO> users = userService.getUsersByTenant(tenantId);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean isActive) {
        
        UserDTO updatedUser = userService.updateUser(id, fullName, role, isActive);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}