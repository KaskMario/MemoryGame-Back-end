package com.example.memoryGame.controller;

import com.example.memoryGame.model.Role;
import com.example.memoryGame.model.User;
import com.example.memoryGame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>>getRoles(){
        List<Role> roles = userService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User>getUserByUsername(@RequestParam String username){
        User user = userService.getUser(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<User> saveNewUser(@RequestParam String username,@RequestParam String password){
        User newUser = userService.saveNewUser(username, password);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam int userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }


}
