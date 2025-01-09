package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.UserException;
import com.app.model.User;
import com.app.response.LoginResponse;
import com.app.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserException {
		return new ResponseEntity<User>(userService.register(user),HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody User user) throws UserException {
		return new ResponseEntity<LoginResponse>(userService.verify(user),HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<User> profile(@RequestHeader("Authorization") String token){
		return new ResponseEntity<User>(userService.getProfile(token),HttpStatus.OK);
	}
}
