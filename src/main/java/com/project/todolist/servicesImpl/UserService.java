package com.project.todolist.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.todolist.dao.UserRepo;
import com.project.todolist.model.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo ;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not Found!!"));
		CustomUserDetail customUserDetail = new CustomUserDetail(user);
		return customUserDetail;
	}

}
