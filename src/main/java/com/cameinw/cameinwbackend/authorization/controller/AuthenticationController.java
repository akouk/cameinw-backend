package com.cameinw.cameinwbackend.authorization.controller;

import com.cameinw.cameinwbackend.authorization.request.LoginRequest;
import com.cameinw.cameinwbackend.authorization.request.RegisterRequest;
import com.cameinw.cameinwbackend.authorization.response.AuthenticationResponse;
import com.cameinw.cameinwbackend.authorization.service.AuthenticationService;
import com.cameinw.cameinwbackend.exception.ResourceAlreadyExistException;
import com.cameinw.cameinwbackend.exception.ResourceNotFoundException;
import com.cameinw.cameinwbackend.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register") //!!CHECK OK!!
    public ResponseEntity<Object> register(
            @Valid  @RequestBody RegisterRequest registerRequest
    ) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isEmpty() &&
                userRepository.findByTheUserName(registerRequest.getTheUserName()).isEmpty())
        {
            return ResponseEntity.ok(authenticationService.register(registerRequest));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exist");
        }
    }


    @PostMapping("/login") //!!CHECK OK!!
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        // Check if the user exists, if not, throw ResourceNotFoundException
        if (!userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
