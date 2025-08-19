package com.project.todolist.services;

import com.project.todolist.payloads.AuthResponse;
import com.project.todolist.payloads.UserLoginDTO;
import com.project.todolist.payloads.UserRegisterDTO;

public interface AuthService {
	String register(UserRegisterDTO registerDTO);
    AuthResponse login(UserLoginDTO loginDTO);
    AuthResponse getAccessToken(String refreshToken);
    void loggedOut();
}
