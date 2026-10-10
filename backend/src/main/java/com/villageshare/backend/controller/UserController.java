package com.villageshare.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.villageshare.backend.entity.User;
import com.villageshare.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("VILLAGER");
        }

        return userRepository.save(user);
    }

    // Login user
    @PostMapping("/login")
    public User loginUser(@RequestBody User loginUser) {

        User user = userRepository.findAll()
                .stream()
                .filter(u ->
                    u.getEmail().equals(loginUser.getEmail())
                    &&
                    u.getPassword().equals(loginUser.getPassword())
                )
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        return user;
    }
    @GetMapping
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}