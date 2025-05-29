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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public UserResponse updateUser(Long id, UserEditRequest editRequest) {
        User user = getUserOrThrow(id);

        BeanUtils.copyProperties(editRequest, user, getNullPropertyNames(editRequest));

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeRequest request, Authentication authentication) {
        User user = getCurrentUserOrThrow(authentication);
        if(!Objects.equals(request.password(), request.repeatPassword())){
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

    private User getUserOrThrow(Long id){
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

    private User getUserByUsernameOrThrow(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("User with username: " + username + " not found"));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private void validateUniqueFields(UserCreateRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new AlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByPhoneNumber(request.phoneNumber()).isPresent()) {
            throw new AlreadyExistsException("This phone number is already taken");
        }
    }

}
