package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.UserAlreadyExistsException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    public final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AuthService(UserRepo userRepo, AuthenticationManager authenticationManager){
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
    }

    public Users register(Users user) {
        Users registeredUser = userRepo.findUsersByUsername(user.getUsername());
        if(registeredUser != null){
            System.out.println("User already exists!");
            throw new UserAlreadyExistsException("User with username : " + user.getUsername() + " already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Users login(Users user) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        return user;
    }
}
