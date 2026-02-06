package com.supplychain.tenant_service.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class TenantContextFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            
            TenantContext.clear();
            
            
            if (request.getRequestURI().startsWith("/api/auth/") || 
                request.getRequestURI().startsWith("/api/public/")) {
                filterChain.doFilter(request, response);
                return;
            }
            
            
            filterChain.doFilter(request, response);
            
        } finally {
            
            TenantContext.clear();
        }
    }
}
