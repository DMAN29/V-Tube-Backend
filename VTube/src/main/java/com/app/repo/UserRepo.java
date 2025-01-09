package com.app.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.User;

public interface UserRepo extends MongoRepository<User, String> {

	User findByEmail(String email);

}
