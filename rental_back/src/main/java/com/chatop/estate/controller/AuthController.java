package com.chatop.estate.controller;

import com.chatop.estate.model.Users;
import com.chatop.estate.payloads.requests.LoginRequest;
import com.chatop.estate.payloads.requests.RegisterRequest;
import com.chatop.estate.payloads.responses.JwtResponse;
import com.chatop.estate.payloads.responses.UserMeResponse;
import com.chatop.estate.repository.IUsersRepository;
import com.chatop.estate.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
    private IUsersRepository usersRepository;

    AuthenticationManager authenticationManager;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    @Autowired
    public AuthController(IUsersRepository usersRepository, AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.usersRepository = usersRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Login
     * @param loginRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with email and password")
    @ApiResponse(responseCode = "200", description = "Login success", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "401", description = "Login failed", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> authUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JSONPObject("message", e.getMessage()));
        }
    }

    /**
     * Register
     * @param registerRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register with name, email and password")
    @ApiResponse(responseCode = "200", description = "Register success", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "400", description = "Register failed", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (!usersRepository.findByEmail(registerRequest.getEmail()).isEmpty()) {
                throw new Exception();
            }

            Users user = new Users(registerRequest.getName(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()));
            usersRepository.save(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            //String token = authServices.createUser(name, email, password);
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Get user by token
     * @param authentication
     * @return
     */
    @ResponseBody
    @GetMapping("/me")
    @Operation(summary = "Get user", description = "Get user object by token")
    @ApiResponse(responseCode = "200", description = "Get user success", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserMeResponse.class)))
    @ApiResponse(responseCode = "401", description = "Get user failed", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> getUser(Authentication authentication) {
        try {
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                Users user = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new Exception("User not found"));
                return ResponseEntity.status(HttpStatus.OK).body(new UserMeResponse(user));
            }
            throw new Exception();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
