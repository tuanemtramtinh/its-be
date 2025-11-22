package com.tuanemtramtinh.itslearningmanagement.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearningContentManagementFacade {

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/test")
    public String testAuthentication() {
        return "Hello World! Auth";
    }
}
