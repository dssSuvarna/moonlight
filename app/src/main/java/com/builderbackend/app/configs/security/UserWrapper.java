package com.builderbackend.app.configs.security;

import org.springframework.security.core.userdetails.UserDetails;
import com.builderbackend.app.models.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * TODO
 * 
 * 1) add different roles so we can have more fine grain controll of permissions
 */


public class UserWrapper implements UserDetails {
    
    private final User user; // Your existing User object

    public UserWrapper(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return roles/permissions as authorities.
        // For simplicity, let's return a single authority.
       // return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
       return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement expiration logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement locking logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement expiration logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement activation logic here
    }
    
}
