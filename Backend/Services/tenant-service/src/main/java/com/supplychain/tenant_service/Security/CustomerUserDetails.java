package com.supplychain.tenant_service.Security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.supplychain.tenant_service.model.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CustomerUserDetails implements UserDetails {
    private UUID id;
    private UUID tenantId;
    private String email;
    private String password;
    private String role;
    private boolean isActive;

    public static CustomerUserDetails fromUser(User user){
        return new CustomerUserDetails(user.getId(), user.getTenantId(), user.getEmail(), user.getPasswordHash(), user.getUserRole().name(), user.getIsActive());
    }

     @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

}
