package com.supplychain.tenant_service.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(UUID id) {
        super("Tenant not found with id: " + id);
    }
    
    public TenantNotFoundException(String subdomain) {
        super("Tenant not found with subdomain: " + subdomain);
    }
}