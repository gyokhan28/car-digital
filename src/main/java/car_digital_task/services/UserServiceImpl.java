package car_digital_task.services;

import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.exceptions.AlreadyExistsException;
import car_digital_task.exceptions.NotFoundException;
import car_digital_task.mapper.UserMapper;
import car_digital_task.models.User;
import car_digital_task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        if (userRepository.findByUsername(userCreateRequest.userName()).isPresent()) {
            throw new AlreadyExistsException("Username is already taken");
        }
        User user = UserMapper.toEntity(userCreateRequest);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found!"));
        return UserMapper.toResponse(user);
    }
}
