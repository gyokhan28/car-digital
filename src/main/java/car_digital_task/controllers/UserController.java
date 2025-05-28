package car_digital_task.controllers;

import car_digital_task.dto.PasswordChangeRequest;
import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserEditRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.create(userCreateRequest));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.getUsers(search));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> edit(@PathVariable("id") Long id, @RequestBody UserEditRequest editRequest) {
        return ResponseEntity.ok(userService.updateUser(id, editRequest));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, Authentication authentication){
        userService.changePassword(passwordChangeRequest, authentication);
    }
}
