package com.app.response;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.Comment;

public interface CommentRepo extends MongoRepository<Comment, String> {

}
