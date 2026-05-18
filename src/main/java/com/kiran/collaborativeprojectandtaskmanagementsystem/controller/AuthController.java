package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ApiResponse;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.AuthService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Users>> register(@RequestBody Users user){
        Users registeredUser = authService.register(user);

        ApiResponse<Users> response = new ApiResponse<>(
                true,
                "Registration successful",
                registeredUser
        );

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody Users user){
        String token = authService.login(user);

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Login successful",
                token
        );

        return ResponseEntity.status(200).body(response);
    }
}
