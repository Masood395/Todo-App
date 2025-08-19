package com.project.todolist.payloads;

import com.project.todolist.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

	@NotBlank(message = "User Name is Required")
	@Size(min = 5,max=20)
	private String userName;
//	   @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$",message = "Email must be a valid .com email")
	@Email(message = "Invalid Email")
	@NotBlank(message = "Email is Required")
	private String email;
    @Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	private Role role;
}
