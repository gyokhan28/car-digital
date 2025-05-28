package car_digital_task.mapper;

import car_digital_task.config.SecurityConfig;
import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.enums.RoleType;
import car_digital_task.models.Role;
import car_digital_task.models.User;

public class UserMapper {
    public static User toEntity(UserCreateRequest request) {

        RoleType roleUser = RoleType.ROLE_USER;

        Role role = Role.builder()
                .id(roleUser.getId())
                .name(roleUser.name())
                .build();

        return User.builder()
                .username(request.username())
                .password(SecurityConfig.passwordEncoder().encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .birthDate(request.birthDate())
                .role(role)
                .isEnabled(true)
                .build();
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getBirthDate());
    }
}
