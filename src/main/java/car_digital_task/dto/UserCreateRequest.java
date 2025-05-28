package car_digital_task.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotNull
        @Size(min = 3, max = 40)
        String username,

        @NotNull @Size(min = 3)
        String password,

        @NotNull
        @Size(min = 3, max = 40)
        String firstName,

        @NotNull
        @Size(min = 3, max = 40)
        String lastName,

        @Email
        @NotEmpty(message = "Email cannot be empty")
        @Column(unique = true)
        String email,

        @NotEmpty(message = "Phone number cannot be empty")
        @Column(unique = true)
        String phoneNumber,

        @NotNull
        LocalDate birthDate
) {
}
