package com.tuanemtramtinh.itsusers.components;

import com.tuanemtramtinh.itsusers.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserManagementComponent {

    private final UserRepository userRepository;

    public UserManagementComponent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUser(String id){
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user with id " + id);
        }
    }
}
