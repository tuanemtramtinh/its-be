package com.tuanemtramtinh.itsusers.presentation;

import com.tuanemtramtinh.itsusers.services.AuthenticationService;
import com.tuanemtramtinh.itsusers.services.UserManagementService;
import com.tuanemtramtinh.itsusers.services.UserService;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;
import com.tuanemtramtinh.itsusers.dto.UserRequest;
import com.tuanemtramtinh.itscommon.dto.UserResponse;
import com.tuanemtramtinh.itscommon.entity.CourseInstance;
import com.tuanemtramtinh.itscommon.response.ApiResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserManagementFacade {

    private final AuthenticationService authenticationService;
    private final UserManagementService userManagementService;
    private final UserService userService;

    public UserManagementFacade(AuthenticationService authenticationService,
            UserManagementService userManagementService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userManagementService = userManagementService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUserLists() {
        return ResponseEntity
                .ok(ApiResponse.ok("Get User list successfully", this.userManagementService.getUserList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetail(@PathVariable String id) {
        return ResponseEntity
                .ok(ApiResponse.ok("Get User detail successfully", this.userManagementService.getUserDetail(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userManagementService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Delete user with id: " + id + " successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUserInfo(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity
                .ok(ApiResponse.ok("Get current user info successfully", userService.getCurrentUserInfo(userId)));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUserInfo(@RequestHeader("X-User-Id") String userId,
            @RequestBody UserRequest data) {
        return ResponseEntity
                .ok(ApiResponse.ok("Update User Info successfully", userService.updateCurrentUserInfo(userId, data)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest req) {
        if (req == null || req.getEmail() == null || req.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Email and password are required"));
        }

        RegisterResponse result = authenticationService.register(req);
        return ResponseEntity.ok(ApiResponse.ok("Register new user successfully", result));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest req) {
        if (req == null || req.getEmail() == null || req.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Email and password are required"));
        }

        LoginResponse response = authenticationService.login(req);
        return ResponseEntity.ok(ApiResponse.ok("Login successfully", response));
    }

}
