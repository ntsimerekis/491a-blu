package com.blu.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        // Insert data from the controller
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));}
}
