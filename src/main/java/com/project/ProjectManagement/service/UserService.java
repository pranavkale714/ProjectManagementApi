package com.project.ProjectManagement.service;


import com.project.ProjectManagement.dto.GetAllUsersResponseDto;
import com.project.ProjectManagement.dto.UserLoginRequestDto;
import com.project.ProjectManagement.dto.UserLoginResponseDto;
import com.project.ProjectManagement.dto.UserRegistrationRequestDto;
import com.project.ProjectManagement.exceptions.UserAlreadyExistsException;
import com.project.ProjectManagement.exceptions.WrongCredentialException;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import com.project.ProjectManagement.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtility;

    public void registerNewUser(UserRegistrationRequestDto payload) {
        if (userRepo.existsByEmailAddress(payload.getUserEmail())) {
            logger.warn("User with email {} already exists", payload.getUserEmail());
            throw new UserAlreadyExistsException("User with email " + payload.getUserEmail() + " already exists");
        }

        User newUser = payload.toEntity();
        newUser.setPasswordHash(passwordEncoder.encode(payload.getUserPassword()));
        userRepo.save(newUser);

        logger.info("User registered successfully with email: {}", payload.getUserEmail());
    }

    public UserLoginResponseDto authenticateUser(UserLoginRequestDto loginDto) {
        logger.info("Authenticating user with email: {}", loginDto.getUserEmail());
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUserEmail(),
                            loginDto.getUserPassword()
                    )
            );
            logger.info("Authentication successful for email: {}", loginDto.getUserEmail());
        } catch (Exception ex) {
            logger.error("Authentication failed for email: {}", loginDto.getUserEmail(), ex);
            throw new WrongCredentialException("Invalid email or password");
        }

        String jwtToken = jwtUtility.createToken(loginDto.getUserEmail());
        logger.info("JWT token generated for email: {}", loginDto.getUserEmail());

        return new UserLoginResponseDto("Login successful", jwtToken);
    }

    public List<GetAllUsersResponseDto> fetchAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> GetAllUsersResponseDto.builder()
                        .id(user.getUserId())
                        .username(user.getFullName())
                        .email(user.getEmailAddress())
                        .build())
                .collect(Collectors.toList());
    }
}
