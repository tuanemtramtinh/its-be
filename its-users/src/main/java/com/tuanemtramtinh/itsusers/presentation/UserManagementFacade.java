package com.tuanemtramtinh.itsusers.presentation;

import com.tuanemtramtinh.itsusers.components.AuthenticationComponent;
import com.tuanemtramtinh.itsusers.components.UserManagementComponent;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;
import com.tuanemtramtinh.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementFacade {

    private final AuthenticationComponent authenticationComponent;
    private final UserManagementComponent  userManagementComponent;

    @Autowired
    public UserManagementFacade(AuthenticationComponent authenticationComponent, UserManagementComponent userManagementComponent) {
        this.authenticationComponent = authenticationComponent;
        this.userManagementComponent = userManagementComponent;
    }

    @GetMapping("/")
    public String index() {
        return "HelloWorld";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest req) {
        try {
            RegisterResponse result = authenticationComponent.register(req);
            return ResponseEntity.ok(ApiResponse.ok("Register new user successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest req) {
        try {
            LoginResponse response = authenticationComponent.login(req);
            return ResponseEntity.ok(ApiResponse.ok("Login successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser (@PathVariable String id){
        try {
            userManagementComponent.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.ok("Delete user with id: " + id + " successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

}
