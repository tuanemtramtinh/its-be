package com.tuanemtramtinh.itsusers.presentation;

import com.tuanemtramtinh.itsusers.components.AuthenticationComponent;
import com.tuanemtramtinh.itsusers.dto.LoginRequest;
import com.tuanemtramtinh.itsusers.dto.LoginResponse;
import com.tuanemtramtinh.itsusers.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementFacade {

    private AuthenticationComponent authenticationComponent;

    @Autowired
    public UserManagementFacade(AuthenticationComponent authenticationComponent) {
        this.authenticationComponent = authenticationComponent;
    }

    @GetMapping("/")
    public String index() {
        return "HelloWorld";
    }

    @GetMapping("/hi")
    public String test() {
        return "Vl";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest req) {
        try {
            String result = authenticationComponent.register(req);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest req) {
        try {
            LoginResponse response = authenticationComponent.login(req);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/register")
    public String registerUser() {
        return "Test";
    }

}
