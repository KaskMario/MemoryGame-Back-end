package com.example.memoryGame.service;

import com.example.memoryGame.dao.RoleRepository;
import com.example.memoryGame.dao.UserRepository;
import com.example.memoryGame.model.Role;
import com.example.memoryGame.model.RoleName;
import com.example.memoryGame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> login(User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser.isPresent() && passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            return foundUser;
        }
        return Optional.empty();
    }

    public Optional<User> register(User user, RoleName roleName) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Optional.empty();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        return Optional.of(userRepository.save(user));
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}