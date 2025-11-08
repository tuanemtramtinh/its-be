package com.tuanemtramtinh.itslearningmanagement.presentations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearningManagementPresentation {
    @GetMapping("/")
    public String index(){
        return "Learning Management";
    }
}
