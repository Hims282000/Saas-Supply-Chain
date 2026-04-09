package com.supplychain.tenant_service.Security;


import com.supplychain.tenant_service.Repository.UserRepository;
import com.supplychain.tenant_service.model.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithTenant(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }
        
        return user;
    }
}
