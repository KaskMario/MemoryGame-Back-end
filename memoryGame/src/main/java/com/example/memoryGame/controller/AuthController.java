package com.example.memoryGame.controller;

import com.example.memoryGame.model.Role;
import com.example.memoryGame.model.RoleName;
import com.example.memoryGame.model.User;
import com.example.memoryGame.service.JwtTokenService;
import com.example.memoryGame.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final LoginService loginService;
    private static final String ADMIN_KEY = "ventilaator";

    public AuthController(JwtTokenService jwtTokenService, LoginService loginService) {
        this.jwtTokenService = jwtTokenService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return loginService.login(user)
                .map(loggedInUser -> {
                    Role role = loggedInUser.getRole();
                    List<String> roles = Collections.singletonList(role.getRole().name());
                    String token = jwtTokenService.generateToken(loggedInUser, roles);
                    return new ResponseEntity<>(token, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        return loginService.register(user, RoleName.USER)
                .map(registeredUser -> {
                    List<String> roles = Collections.singletonList(registeredUser.getRole().getRole().name());
                    String token = jwtTokenService.generateToken(registeredUser, roles);

                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User registered successfully");
                    response.put("token", token);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User already exists");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                });
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody User user, @RequestParam String adminKey) {
        if (!ADMIN_KEY.equals(adminKey)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid admin key");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return loginService.register(user, RoleName.ADMIN)
                .map(registeredAdmin -> {
                    List<String> roles = Collections.singletonList(registeredAdmin.getRole().getRole().name());
                    String token = jwtTokenService.generateToken(registeredAdmin, roles);

                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Admin registered successfully");
                    response.put("token", token);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User already exists");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                });
    }
}