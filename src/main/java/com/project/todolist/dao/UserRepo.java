package com.project.todolist.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.todolist.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	
	Optional<User> findByUserName(String userName);
	Optional<User> findByEmail(String email);
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	Optional<User> findByRefreshToken(String refreshToken);
}
