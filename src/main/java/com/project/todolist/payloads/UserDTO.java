package com.project.todolist.payloads;

import com.project.todolist.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private String userName;
	private String email;
	private Role role;

}
