package com.supplychain.tenant_service.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.supplychain.tenant_service.model.Entity.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest httpServletRequest,
        @NonNull HttpServletResponse httpServletResponse,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            try {
                final String jwt = authHeader.substring(7);
                final String userEmail = jwtTokenProvider.getEmailFromToken(jwt);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                    if (jwtTokenProvider.validateToken(jwt)) {
                        // Use DB-verified tenant from User entity, not JWT claim
                        User user = (User) userDetails;
                        TenantContext.setCurrentTenant(user.getTenant().getId());

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        log.debug("Authenticated user: {} for tenant: {}", userEmail, user.getTenant().getId());
                    }
                }
            } catch (ExpiredJwtException e) {
                log.warn("JWT token expired: {}", e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write("{\"error\": \"Token expired\"}");
                return;
            } catch (JwtException e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
        String path = httpServletRequest.getServletPath();
        return path.startsWith("/api/auth/signup") || path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/register") || path.startsWith("/actuator/health");
    }
}
