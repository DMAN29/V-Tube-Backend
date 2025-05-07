package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.exception.UserException;
import com.app.model.User;
import com.app.repo.UserRepo;
import com.app.response.LoginResponse;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public User register(User user) throws UserException {
		User existedUser = userRepo.findByEmail(user.getEmail());
		if(existedUser != null) {
			throw new UserException("User with email id already Exist");
		}
		user.setPassword(encoder.encode(user.getPassword()));
		user.setFullName(user.getFirstName()+ " "+user.getLastName());
		return userRepo.save(user);
	}

	public LoginResponse verify(User user) throws UserException {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
		if(authentication.isAuthenticated()) {
			LoginResponse response = new LoginResponse(user.getEmail(),jwtService.generateToken(user.getEmail()),HttpStatus.OK);
			return response;
		}
		throw new UserException("Unauthorized");
	}

	public User getProfile(String token) {
		String username = jwtService.extractUserName(token);
		return userRepo.findByEmail(username);
	}
	
	public User getUserById(String id) throws UserException {
		return userRepo.findById(id).orElseThrow(()-> new UserException("User with Id :"+id+" not found"));
	}
	
	public User getUserByEmail(String email) throws UserException{
		User user = userRepo.findByEmail(email);
		if( user == null) {
			throw new UserException("User with email not found");
		}
		return user;
	}
	
	public User updateUser(User user) {
		return userRepo.save(user);
	}

	
	
}
