package com.app.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.Video;

public interface VideoRepo extends MongoRepository<Video, String>{
	

}
