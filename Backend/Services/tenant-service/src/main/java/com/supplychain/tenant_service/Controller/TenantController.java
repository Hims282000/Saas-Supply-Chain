package com.supplychain.tenant_service.Controller;


import com.supplychain.tenant_service.model.Dto.TenantDTO;
import com.supplychain.tenant_service.Security.TenantContext;
import com.supplychain.tenant_service.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management", description = "Tenant management endpoints")
@SecurityRequirement(name = "bearer-key")
public class TenantController {
    
    private final TenantService tenantService;
    
    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable UUID id) {
        TenantDTO tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(tenant);
    }
    
    @GetMapping("/subdomain/{subdomain}")
    @Operation(summary = "Get tenant by subdomain")
    public ResponseEntity<TenantDTO> getTenantBySubdomain(@PathVariable String subdomain) {
        TenantDTO tenant = tenantService.getTenantBySubdomain(subdomain);
        return ResponseEntity.ok(tenant);
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current tenant info")
    public ResponseEntity<TenantDTO> getCurrentTenant() {
        UUID tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        TenantDTO tenant = tenantService.getTenantById(tenantId);
        return ResponseEntity.ok(tenant);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update tenant configuration")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDTO> updateTenant(
            @PathVariable UUID id,
            @RequestBody TenantDTO.TenantConfigDTO configDTO) {
        TenantDTO updatedTenant = tenantService.updateTenant(id, configDTO);
        return ResponseEntity.ok(updatedTenant);
    }
    
    @GetMapping
    @Operation(summary = "Get all tenants (Super Admin only)")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        List<TenantDTO> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }
}
