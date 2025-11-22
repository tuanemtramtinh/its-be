package com.tuanemtramtinh.itsusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.tuanemtramtinh.itsusers", "com.tuanemtramtinh.utils",
        "com.tuanemtramtinh.security" })
public class ItsUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsUsersApplication.class, args);
    }

}
