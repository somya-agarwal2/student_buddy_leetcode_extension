package com.studentbuddy.security;

import com.studentbuddy.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * CustomUserDetails wraps the User entity and implements Spring Security's UserDetails.
 * This allows your User entity to work with authentication, JWT, and security features.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Return roles/authorities for the user.
     * Currently defaults to ROLE_USER; you can extend later to map real roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Always non-expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Always non-locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Always valid
    }

    @Override
    public boolean isEnabled() {
        return true; // Always enabled
    }

    /**
     * Optional: get the underlying User entity
     */
    public User getUser() {
        return user;
    }

    /**
     * Optional: get the User ID directly
     */
    public Long getId() {
        return user.getId();
    }
}
