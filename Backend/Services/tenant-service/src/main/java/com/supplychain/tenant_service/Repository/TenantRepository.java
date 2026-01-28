package com.supplychain.tenant_service.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.supplychain.tenant_service.model.Entity.Tenant;

@Repository
public class TenantRepository {
    Optional<Tenant> findBySubDomain(String subdomain);

    boolean existsBySubDomain(String subdomain);
    
}
