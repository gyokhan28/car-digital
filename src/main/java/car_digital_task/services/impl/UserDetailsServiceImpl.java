package car_digital_task.services.impl;

import car_digital_task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }
}
