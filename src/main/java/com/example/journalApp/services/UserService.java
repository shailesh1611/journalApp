package com.example.journalApp.services;

import com.example.journalApp.entity.User;
import com.example.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User addAdmin(User user) {
        user.setRoles(List.of("USER", "ADMIN"));
        return userRepository.save(user);
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public void deleteUserByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

}
