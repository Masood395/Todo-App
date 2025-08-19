package com.project.todolist.servicesImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.todolist.dao.UserRepo;
import com.project.todolist.model.User;
import com.project.todolist.payloads.AuthResponse;
import com.project.todolist.payloads.UserLoginDTO;
import com.project.todolist.payloads.UserRegisterDTO;
import com.project.todolist.security.JwtUtil;
import com.project.todolist.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
    private  UserRepo ur;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
    private  JwtUtil jwtUtil;
	@Autowired
    private   AuthenticationManager authenticationManager;
	@Autowired
    private  ModelMapper modelMapper;

    @Override
    public String register(UserRegisterDTO registerDTO) {
    	String trim = registerDTO.getUserName().trim();
		if(trim.length()<5) {
			throw new IllegalArgumentException("size must be min. 5");
		}
		registerDTO.setUserName(trim);
        if (ur.existsByUserName(registerDTO.getUserName())) {
            throw new RuntimeException("Username already exists");
        }
        if (ur.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = modelMapper.map(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        ur.save(user);

        return "User registered successfully";
    }

    @Override
    public AuthResponse login(UserLoginDTO loginDTO) {
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        User user = ur.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

//        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(),true);
        String refreshToken = jwtUtil.generateToken(user.getUserName(), user.getRole().name(),false);
        user.setRefreshToken(refreshToken);
        ur.save(user);
        return new AuthResponse(accessToken,refreshToken);
    }
    
    @Override
    public AuthResponse getAccessToken(String refreshToken) {
    	User user = ur.findByRefreshToken(refreshToken)
    		.orElseThrow(() -> new RuntimeException("Invalid refresh token"));
		
    	if(jwtUtil.validateToken(refreshToken)) 
    	{
    	String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(),true);
    	refreshToken=jwtUtil.generateToken(user.getUserName(), user.getRole().name(),false);
    	user.setRefreshToken(refreshToken);
        ur.save(user);
    	return new AuthResponse(accessToken,refreshToken);
    	}
    	throw new IllegalArgumentException("Token is Expired!!");
    }
    
    @Override
    public void loggedOut() {
    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    User user = ur.findByEmail(email)
	                 .orElseThrow(() -> new RuntimeException("User not found"));
    	user.setRefreshToken(null);
    	ur.save(user);
    }
}

