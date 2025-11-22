package com.tuanemtramtinh.itslearningmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.tuanemtramtinh.itslearningmanagement", "com.tuanemtramtinh.utils",
        "com.tuanemtramtinh.security" })
public class ItsLearningManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsLearningManagementApplication.class, args);
    }

}
