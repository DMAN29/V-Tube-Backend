package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.exception.CommentException;
import com.app.exception.VideoException;
import com.app.model.Comment;
import com.app.model.User;
import com.app.model.Video;
import com.app.response.CommentRepo;

@Service
public class CommentService {

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VideoService videoService;

	public Comment postComment(Comment comment,String videoId, String token) throws VideoException {
		Video video = videoService.getVideoById(videoId);
		User user = userService.getProfile(token);
		List<String> userComments = user.getComments();
		List<String> videoComments = video.getComments();
		
		comment.setUser(user.getEmail());
		comment.setVideoId(videoId);
		
		Comment saveComment  = commentRepo.save(comment); 
		userComments.add(saveComment.getCId());
		userService.updateUser(user);
		
		videoComments.add(saveComment.getCId());
		videoService.updateVideo(video);
		
		return saveComment;
	}

	public List<Comment> getAll() {
		return commentRepo.findAll();
	}

	public Comment getCommentById(String id) throws CommentException {
		return commentRepo.findById(id).orElseThrow(()-> new CommentException("Comment with id : "+id+" not found"));
	}

	public List<Comment> getCommentsOnVideo(String id) throws VideoException, CommentException {
		List<Comment> comments = new ArrayList<>();
		Video video = videoService.getVideoById(id);
		for(String commentId: video.getComments()) {
			Comment comment = getCommentById(commentId);
			comments.add(comment);
		}
		return comments;
	}

	public String deleteComment(String id, String token) throws CommentException, VideoException {
		User user = userService.getProfile(token);
		Comment comment = getCommentById(id);
		Video video = videoService.getVideoById(comment.getVideoId());
		if (user.getEmail().equals(comment.getUser()) || user.getEmail().equals(video.getUser())) {
		        commentRepo.deleteById(id);
		        
		        List<String> videoCommets = video.getComments();
		        List<String> userComments = user.getComments();
		        if(videoCommets!=null && userComments!=null && userComments.contains(id)&& videoCommets.contains(id)) {
		        	videoCommets.remove(id);
		        	video.setComments(videoCommets);
		        	videoService.updateVideo(video);
		        	
		        	userComments.remove(id);
		        	user.setComments(userComments);
		        	userService.updateUser(user);
		        }
		        return "Comment with id: " + id + " has been deleted successfully.";
		    } else {
		        throw new CommentException("Comment with id: " + id + " not found");
		    }
	}

	public String deleteComment(String id) throws CommentException {
		 if (commentRepo.existsById(id)) {
		        commentRepo.deleteById(id);
		        return "Comment with id: " + id + " has been deleted successfully.";
		    } else {
		        throw new CommentException("Comment with id: " + id + " not found");
		    }
	}

}

