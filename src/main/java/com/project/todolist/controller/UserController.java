package com.project.todolist.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.todolist.dao.UserRepo;
import com.project.todolist.model.User;
import com.project.todolist.payloads.ApiResponse;
import com.project.todolist.payloads.UserDTO;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserRepo ur;
	
	@Autowired
	ModelMapper modelMapper; 

	@GetMapping
	public ResponseEntity<?> getUsers(){
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
//		System.out.println(SecurityContextHolder.getContext().getAuthentication());
	     User user= ur.findByEmail(email)
	                 .orElseThrow(() -> new RuntimeException("User not found"));
		return ResponseEntity.ok(new ApiResponse<>(true, modelMapper.map(user, UserDTO.class)));
	}
}
