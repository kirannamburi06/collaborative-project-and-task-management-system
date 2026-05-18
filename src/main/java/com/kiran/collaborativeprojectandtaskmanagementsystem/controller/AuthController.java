package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ApiResponse;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication APIs",
        description = "Manage authentication - register, and login"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(
            summary = "Register",
            description = "Create an account with a email/username, and a password"
    )
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

    @Operation(
            summary = "Login",
            description = "Login to account with the registered credentials. It returns a JWT token"
    )
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
