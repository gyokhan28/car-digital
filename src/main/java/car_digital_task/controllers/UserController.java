package car_digital_task.controllers;

import car_digital_task.dto.PasswordChangeRequest;
import car_digital_task.dto.UserCreateRequest;
import car_digital_task.dto.UserEditRequest;
import car_digital_task.dto.UserResponse;
import car_digital_task.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> create(
            @Parameter(description = "User creation data", required = true)
            @Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.create(userCreateRequest));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves user data by given ID")
    @ApiResponse(responseCode = "200", description = "User found and returned")
    @ApiResponse(responseCode = "404", description = "User with specified ID not found", content = @Content)
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getById(
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Get list of users", description = "Returns a list of users with optional search and pagination")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> getUsers(
            @Parameter(description = "Search text for filtering users", required = false)
            @RequestParam(required = false) String search,
            @Parameter(description = "Page number, starting from 0", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false)
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsers(search, page, size));
    }

    @Operation(summary = "Edit current logged-in user", description = "Updates fields of the current authenticated user")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> edit(
            @Parameter(description = "User edit data", required = true)
            @RequestBody UserEditRequest editRequest,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(editRequest, authentication));
    }

    @Operation(summary = "Edit user by ID (admin only)", description = "Admin can update any user's data")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "403", description = "Access denied to edit this user", content = @Content)
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> editUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "User edit data", required = true)
            @RequestBody UserEditRequest editRequest,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUserById(id, editRequest, authentication));
    }

    @Operation(summary = "Change password of current user", description = "Changes password of the currently authenticated user")
    @ApiResponse(responseCode = "204", description = "Password changed successfully")
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "Password change data", required = true)
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            Authentication authentication) {
        userService.changePassword(passwordChangeRequest, authentication);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete user by ID (admin only)", description = "Admin can delete any user by their ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "403", description = "Access denied to delete this user", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
