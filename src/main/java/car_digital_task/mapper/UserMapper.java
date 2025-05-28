package car_digital_task.mapper;

import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.models.User;

public class UserMapper {
    public static User toEntity(UserCreateRequest request) {
        return User.builder()
                .username(request.userName())
                .password(request.password())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .birthDate(request.birthDate())
                .isEnabled(true)
                .build();
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getBirthDate());
    }
}
