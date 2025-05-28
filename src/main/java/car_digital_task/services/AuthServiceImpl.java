package car_digital_task.services;

import car_digital_task.dto.LoginRequest;
import car_digital_task.exceptions.AuthenticationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails;
        userDetails = authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(userDetails);
        setJwtCookie(request, response, jwtToken);
    }

    @Override
    public void setJwtCookie(HttpServletRequest request, HttpServletResponse response, String jwtToken) {

        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private UserDetails authenticate(LoginRequest loginRequesto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequesto.username().toLowerCase(), loginRequesto.password());
            Authentication authResult = authenticationManager.authenticate(authentication);
            return (UserDetailsImpl) authResult.getPrincipal();
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Authentication failed: Invalid username or password.");
        }
    }
}