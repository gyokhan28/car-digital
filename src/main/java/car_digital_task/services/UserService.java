package car_digital_task.services;

import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserResponse;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);

    UserResponse getById(Long id);

}
