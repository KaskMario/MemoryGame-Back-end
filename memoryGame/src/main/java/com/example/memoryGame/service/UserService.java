package com.example.memoryGame.service;

import com.example.memoryGame.dao.UserRepository;
import com.example.memoryGame.dao.RoleRepository;
import com.example.memoryGame.model.User;
import com.example.memoryGame.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.memoryGame.model.RoleName.USER;

@Service
public class UserService {

@Autowired
private UserRepository userRepository;
@Autowired
private RoleRepository roleRepository;
@Autowired
private PasswordEncoder passwordEncoder;

    public User saveNewUser(String username, String password){
        User newUser = new User();
        if(userRepository.findByUsername(username).isEmpty()){
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password));
        }
        Role userRole = roleRepository.findByRole(USER)
                .orElseThrow(() -> new RuntimeException("User Role not set."));
        newUser.setRole(userRole);


        return userRepository.save(newUser);
    }


    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void deleteUser(int userId){
        User deletedUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(deletedUser);

    }

    public User updateUser(User user){
        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
