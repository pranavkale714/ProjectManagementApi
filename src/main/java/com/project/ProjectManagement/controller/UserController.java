package com.project.ProjectManagement.controller;


import com.project.ProjectManagement.dto.*;
import com.project.ProjectManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> registerNewUser(@Valid @RequestBody UserRegistrationRequestDto userPayload) {

        userService.registerNewUser(userPayload);

        UserRegistrationResponse response = new UserRegistrationResponse("User registered successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserLoginResponseDto> authenticateUser(@Valid @RequestBody UserLoginRequestDto loginPayload) {
        UserLoginResponseDto response = userService.authenticateUser(loginPayload);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getCurrentUserProfile(Authentication authentication) {
        return new ResponseEntity<>("You are logged in as: " + authentication.getName(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetAllUsersResponseDto>> listAllUsers() {
        List<GetAllUsersResponseDto> users = userService.fetchAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
