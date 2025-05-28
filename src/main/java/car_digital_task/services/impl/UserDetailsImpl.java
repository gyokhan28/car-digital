package car_digital_task.services.impl;

import car_digital_task.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private final Object principal;

    public UserDetailsImpl(User user) {
        this.principal = user;
    }

    public User getUser() {
        return (User) principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getUser().getRole().getName()));
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return getUser().getUsername();
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
        return getUser().isEnabled();
    }
}
