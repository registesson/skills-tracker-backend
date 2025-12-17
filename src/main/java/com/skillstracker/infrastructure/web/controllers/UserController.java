package com.skillstracker.infrastructure.web.controllers;

import com.skillstracker.application.auth.JwtService;
import com.skillstracker.application.user.UserService;
import com.skillstracker.domain.user.User;
import com.skillstracker.infrastructure.web.dto.AuthRequest;
import com.skillstracker.infrastructure.web.dto.RegisterRequest;
import com.skillstracker.infrastructure.web.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {


    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account and generate JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        try {
            User user = userService.createUser(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName());
            String token = jwtService.generateToken(user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegisterResponse(
                            user.getEmail(),
                            token,
                            "Email is already in use"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(null, null, e.getMessage()));
        }

    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email and password, returns JWT token valid for 24h")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    public ResponseEntity<RegisterResponse> login(@RequestBody AuthRequest request) {
        try {
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user.getEmail());
            return ResponseEntity.ok(new RegisterResponse(
                    user.getEmail(),
                    token,
                    "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RegisterResponse(null, null, "Invalid email or password"));
        }
    }
}
