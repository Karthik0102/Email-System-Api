package com.email.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.email.system.dto.UserRegisterDto;
import com.email.system.enums.Role;
import com.email.system.model.User;
import com.email.system.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static final String USERNAME_EXISTS_ERROR = "Username already exists";
	private static final String USERNAME_EMPTY_ERROR = "Username cannot be empty";
	private static final String PASSWORD_EMPTY_ERROR = "Password cannot be empty";
	private static final String PASSWORD_WEAK_ERROR = "Password must be at least 8 characters long, contain a digit, and a special character";
	private static final String ROLE_INVALID_ERROR = "Role must be either USER or ADMIN";

	public String registerUser(UserRegisterDto userDto) {

		if (userDto.getUserName() == null || userDto.getUserName().isEmpty()) {
			return USERNAME_EMPTY_ERROR;
		}

		if (userRepository.findByUserName(userDto.getUserName()) != null) {
			return USERNAME_EXISTS_ERROR;
		}

		if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			return PASSWORD_EMPTY_ERROR;
		}

		if (!isValidPassword(userDto.getPassword())) {
			return PASSWORD_WEAK_ERROR;
		}

		if (userDto.getRole() == null || !isValidRole(userDto.getRole())) {
			return ROLE_INVALID_ERROR;
		}

		String encodedPassword = passwordEncoder.encode(userDto.getPassword());

		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setPassword(encodedPassword);
		user.setRole(userDto.getRole());

		userRepository.save(user);
		return "User Registered Successfully!! ";

	}

	private boolean isValidPassword(String password) {
		// Check if the password meets the criteria
		return password.length() >= 8 && password.matches(".*\\d.*") && // contains at least one digit
				password.matches(".*[^a-zA-Z0-9].*"); // contains at least one special character
	}

	private boolean isValidRole(Role role) {
		// Ensure the role is either USER or ADMIN
		return role == Role.USER || role == Role.ADMIN;
	}

}
