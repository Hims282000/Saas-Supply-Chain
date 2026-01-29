package com.supplychain.tenant_service.Security;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantContext {
    private static final ThreadLocal<UUID> currentTenant= new ThreadLocal<>();
    public static void setTenantId(UUID tenantId){
        currentTenant.set(tenantId);
    }
    public static UUID getTenantId(){
        return currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();

    }

    
}
