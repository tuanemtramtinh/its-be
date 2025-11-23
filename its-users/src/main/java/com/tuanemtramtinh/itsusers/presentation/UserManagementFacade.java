package com.tuanemtramtinh.itsusers.presentation;

import com.tuanemtramtinh.itsusers.services.AuthenticationService;
import com.tuanemtramtinh.itsusers.services.UserManagementService;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import com.tuanemtramtinh.itsusers.dto.RegisterResponse;
import com.tuanemtramtinh.itscommon.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementFacade {

    private final AuthenticationService authenticationService;
    private final UserManagementService userManagementService;

    @Autowired
    public UserManagementFacade(AuthenticationService authenticationService, UserManagementService userManagementService) {
        this.authenticationService = authenticationService;
        this.userManagementService = userManagementService;
    }

    @GetMapping("/")
    public String index() {
        return "HelloWorld";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest req) {
        try {
            RegisterResponse result = authenticationService.register(req);
            return ResponseEntity.ok(ApiResponse.ok("Register new user successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody LoginRequest req) {
        try {
            LoginResponse response = authenticationService.login(req);
            return ResponseEntity.ok(ApiResponse.ok("Login successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser (@PathVariable String id){
        try {
            userManagementService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.ok("Delete user with id: " + id + " successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

}
