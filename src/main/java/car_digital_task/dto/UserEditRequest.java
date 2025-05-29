package car_digital_task.dto;

import java.time.LocalDate;

public record UserEditRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email) {
}