package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.CommentException;
import com.app.exception.VideoException;
import com.app.model.Comment;
import com.app.service.CommentService;

@RestController 
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	
	@PostMapping("/{videoId}")
	public ResponseEntity<Comment> postComment(@RequestBody Comment comment,@PathVariable("videoId") String videoId,@RequestHeader("Authorization") String token) throws VideoException{
		return new ResponseEntity<Comment>(commentService.postComment(comment,videoId,token),HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Comment>> getComment(){
		return new ResponseEntity<List<Comment>>(commentService.getAll(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable("id") String id) throws CommentException{
		return new ResponseEntity<Comment>(commentService.getCommentById(id),HttpStatus.OK);
	}
	
	@GetMapping("/video/{id}")
	public ResponseEntity<List<Comment>> getCommentsOnVideo(@PathVariable("id") String id) throws VideoException, CommentException{
		return new ResponseEntity<List<Comment>>(commentService.getCommentsOnVideo(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable("id") String id,@RequestHeader("Authorization") String token) throws CommentException, VideoException{
		return new ResponseEntity<String>(commentService.deleteComment(id,token),HttpStatus.ACCEPTED);
	}
	
}
