package car_digital_task.services.impl;

import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.exceptions.AlreadyExistsException;
import car_digital_task.exceptions.NotFoundException;
import car_digital_task.mapper.UserMapper;
import car_digital_task.models.User;
import car_digital_task.repositories.UserRepository;
import car_digital_task.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found!"));
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getUsers(String search) {
        List<User> userList;
        if (search == null || search.isBlank()) {
            userList = userRepository.findAllSorted();
        } else {
            userList = userRepository.findAllBySearch(search);
        }
        return userList.stream().map(UserMapper::toResponse).toList();
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
