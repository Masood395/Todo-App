package com.project.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.todolist.payloads.ApiResponse;
import com.project.todolist.payloads.AuthResponse;
import com.project.todolist.payloads.UserLoginDTO;
import com.project.todolist.payloads.UserRegisterDTO;
import com.project.todolist.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody UserRegisterDTO signupRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true,authService.register(signupRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody UserLoginDTO loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,response));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> getAccessToken(@RequestParam String refreshToken) {
    	return ResponseEntity.ok(new ApiResponse<>(true,authService.getAccessToken(refreshToken)));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logOutUser() {
    	authService.loggedOut();
    	return ResponseEntity.ok(new ApiResponse<>(true,"Logged-Out"));
    }
}
