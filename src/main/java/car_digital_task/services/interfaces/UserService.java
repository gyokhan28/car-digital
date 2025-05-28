package car_digital_task.services.interfaces;

import car_digital_task.dto.PasswordChangeRequest;
import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserEditRequest;
import car_digital_task.dto.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest userCreateRequest);

    UserResponse getById(Long id);

    List<UserResponse> getUsers(String search);

    UserResponse updateUser(Long id, UserEditRequest editRequest);

    void changePassword(PasswordChangeRequest request, Authentication authentication);
}
