package com.tuanemtramtinh.itsusers.services;

import com.tuanemtramtinh.itsusers.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserManagementService {

    private final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
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
