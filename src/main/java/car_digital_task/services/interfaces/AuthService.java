package car_digital_task.services.interfaces;

import car_digital_task.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    void setJwtCookie(HttpServletRequest request, HttpServletResponse response, String jwtToken);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
