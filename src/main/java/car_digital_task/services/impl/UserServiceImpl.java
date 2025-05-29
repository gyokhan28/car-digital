package car_digital_task.services.impl;

import car_digital_task.config.SecurityConfig;
import car_digital_task.dto.PasswordChangeRequest;
import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserEditRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.exceptions.AlreadyExistsException;
import car_digital_task.exceptions.AuthenticationFailedException;
import car_digital_task.exceptions.InvalidRequestException;
import car_digital_task.exceptions.NotFoundException;
import car_digital_task.mapper.UserMapper;
import car_digital_task.models.User;
import car_digital_task.repositories.UserRepository;
import car_digital_task.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest userCreateRequest) {
        validateUniqueFields(userCreateRequest);
        User user = UserMapper.toEntity(userCreateRequest);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Username or phone number already exists");
        }
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = getUserOrThrow(id);
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getUsers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending().and(Sort.by("birthDate").ascending()));
        Page<User> userPage;

        List<User> userList;
        if (search == null || search.isBlank()) {
            userPage = userRepository.findAllSorted(pageable);
        } else {
            userPage = userRepository.findAllBySearch(search, pageable);
        }
        return userPage.map(UserMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserEditRequest editRequest, Authentication authentication) {
        User user = getCurrentUserOrThrow(authentication);
        updateChanges(user, editRequest);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUserById(Long id, UserEditRequest editRequest, Authentication authentication) {
        User user = getUserOrThrow(id);
        updateChanges(user, editRequest);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeRequest request, Authentication authentication) {
        User user = getCurrentUserOrThrow(authentication);
        if (!Objects.equals(request.password(), request.repeatPassword())) {
            throw new InvalidRequestException("Password and repeated password do not match!");
        }
        user.setPassword(SecurityConfig.passwordEncoder().encode(request.password()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserOrThrow(id);
        userRepository.delete(user);
    }

    private void updateChanges(User user, UserEditRequest request) {
        updateFirstName(user, request.firstName());
        updateLastName(user, request.lastName());
        updateBirthDate(user, request.dateOfBirth());
        updatePhoneNumber(user, request.phoneNumber());
        updateEmail(user, request.email());
    }

    private boolean doesEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("This email is already registered!");
        }
        return false;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    private User getCurrentUserOrThrow(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationFailedException("User not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUser().getUsername();
        return getUserByUsernameOrThrow(username);
    }

    private User getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User with username: " + username + " not found"));
    }

    private void validateUniqueFields(UserCreateRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new AlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByPhoneNumber(request.phoneNumber()).isPresent()) {
            throw new AlreadyExistsException("This phone number is already taken");
        }
    }

    private void updateFirstName(User user, String firstName) {
        if (firstName != null) {
            user.setFirstName(firstName);
        }
    }

    private void updateLastName(User user, String lastName) {
        if (lastName != null) {
            user.setLastName(lastName);
        }
    }

    private void updateBirthDate(User user, LocalDate birthDate) {
        if (birthDate != null) {
            user.setBirthDate(birthDate);
        }
    }

    private void updatePhoneNumber(User user, String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(phoneNumber)) {
                throw new AlreadyExistsException("This phone number is already registered");
            }
            user.setPhoneNumber(phoneNumber);
        }
    }

    private void updateEmail(User user, String email) {
        if (email != null && !email.equals(user.getEmail())) {
            if (doesEmailExist(email)) {
                throw new AlreadyExistsException("This email is already registered");
            }
            user.setEmail(email);
        }
    }
}
