package com.supplychain.tenant_service.service;


import com.supplychain.tenant_service.Exception.DuplicateEntityException;
import com.supplychain.tenant_service.Exception.TenantNotFoundException;
import com.supplychain.tenant_service.model.Dto.TenantDTO;
import com.supplychain.tenant_service.model.Entity.Tenant;
import com.supplychain.tenant_service.model.eNums.SubscriptionStatus;
import com.supplychain.tenant_service.model.eNums.SubscriptionTier;
import com.supplychain.tenant_service.Repository.TenantRepository;
import com.supplychain.tenant_service.Security.TenantContext;
import com.supplychain.tenant_service.service.event.TenantEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {
    
    private final TenantRepository tenantRepository;
    private final TenantEventPublisher eventPublisher;
    
    @Transactional
    public TenantDTO createTenant(String name, String subdomain, SubscriptionTier subscriptionTier) {
        // Validate uniqueness
        if (tenantRepository.existsBySubdomain(subdomain)) {
            throw new DuplicateEntityException("Subdomain already exists: " + subdomain);
        }
        
        if (tenantRepository.existsByName(name)) {
            throw new DuplicateEntityException("Tenant name already exists: " + name);
        }
        
        // Create tenant config
        Tenant.TenantConfig config = Tenant.TenantConfig.builder()
                .timezone("UTC")
                .currency("USD")
                .emailNotifications(true)
                .smsNotifications(false)
                .maxUsers(getMaxUsersForTier(subscriptionTier))
                .maxShipmentsPerMonth(getMaxShipmentsForTier(subscriptionTier))
                .build();
        
        // Create tenant entity
        Tenant tenant = Tenant.builder()
                .name(name)
                .subdomain(subdomain)
                .subscriptionTier(subscriptionTier)
                .status(SubscriptionStatus.ACTIVE)
                .config(config)
                .build();
        
        Tenant savedTenant = tenantRepository.save(tenant);
        log.info("Created tenant: {} with subdomain: {}", name, subdomain);
        
        // Publish event
        eventPublisher.publishTenantCreated(
                savedTenant.getId(),
                savedTenant.getName(),
                savedTenant.getSubdomain(),
                savedTenant.getSubscriptionTier()
        );
        
        return mapToDTO(savedTenant);
    }
    
    public TenantDTO getTenantById(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));
        return mapToDTO(tenant);
    }
    
    public TenantDTO getTenantBySubdomain(String subdomain) {
        Tenant tenant = tenantRepository.findBySubdomain(subdomain)
                .orElseThrow(() -> new TenantNotFoundException(subdomain));
        return mapToDTO(tenant);
    }
    
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TenantDTO updateTenant(UUID tenantId, TenantDTO.TenantConfigDTO configDTO) {
        UUID currentTenantId = TenantContext.getCurrentTenant();
        if (!tenantId.equals(currentTenantId)) {
            throw new SecurityException("Cannot update another tenant");
        }
        
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));
        
        // Update config
        if (configDTO != null) {
            Tenant.TenantConfig config = Tenant.TenantConfig.builder()
                    .timezone(configDTO.getTimezone())
                    .currency(configDTO.getCurrency())
                    .emailNotifications(configDTO.getEmailNotifications())
                    .smsNotifications(configDTO.getSmsNotifications())
                    .maxUsers(configDTO.getMaxUsers())
                    .maxShipmentsPerMonth(configDTO.getMaxShipmentsPerMonth())
                    .build();
            tenant.setConfig(config);
        }
        
        Tenant updatedTenant = tenantRepository.save(tenant);
        return mapToDTO(updatedTenant);
    }
    
    @Transactional
    public void suspendTenant(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException(tenantId));
        
        tenant.setStatus(SubscriptionStatus.SUSPENDED);
        tenantRepository.save(tenant);
        log.info("Suspended tenant: {}", tenantId);
    }
    
    private TenantDTO mapToDTO(Tenant tenant) {
        TenantDTO.TenantConfigDTO configDTO = null;
        if (tenant.getConfig() != null) {
            configDTO = TenantDTO.TenantConfigDTO.builder()
                    .timezone(tenant.getConfig().getTimezone())
                    .currency(tenant.getConfig().getCurrency())
                    .emailNotifications(tenant.getConfig().getEmailNotifications())
                    .smsNotifications(tenant.getConfig().getSmsNotifications())
                    .maxUsers(tenant.getConfig().getMaxUsers())
                    .maxShipmentsPerMonth(tenant.getConfig().getMaxShipmentsPerMonth())
                    .build();
        }
        
        return TenantDTO.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .subdomain(tenant.getSubdomain())
                .subscriptionTier(tenant.getSubscriptionTier())
                .status(tenant.getStatus())
                .config(configDTO)
                .createdAt(tenant.getCreatedAt())
                .build();
    }
    
    private Integer getMaxUsersForTier(SubscriptionTier tier) {
        return switch (tier) {
            case FREE -> 3;
            case STARTER -> 10;
            case PROFESSIONAL -> 50;
            case ENTERPRISE -> 500;
        };
    }
    
    private Integer getMaxShipmentsForTier(SubscriptionTier tier) {
        return switch (tier) {
            case FREE -> 100;
            case STARTER -> 1000;
            case PROFESSIONAL -> 10000;
            case ENTERPRISE -> 100000;
        };
    }
}
