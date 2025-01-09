package com.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.CommentException;
import com.app.exception.UserException;
import com.app.exception.VideoException;
import com.app.model.User;
import com.app.model.Video;
import com.app.repo.VideoRepo;
import com.app.response.VideoResponse;

@Service
public class VideoService {

	@Autowired
	private VideoRepo videoRepo;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private CommentService commentService;
	
	@Autowired
	private S3Service s3Service;
	
	public VideoResponse uploadVideo(MultipartFile file,String token) {
		String url = s3Service.uploadFile(file);
		User user = userService.getProfile(token);

		Video video = new Video();
		video.setVideoUrl(url);
		video.setUser(user.getEmail());
		Video savedVideo = videoRepo.save(video);
		
		List<String> videos = user.getVideos();
		videos.add(savedVideo.getVideoId());
		user.setVideos(videos);
		userService.updateUser(user);
		
		VideoResponse response = new VideoResponse();
		response.setUser(user.getEmail());
		response.setVideoUrl(url);
		response.setVideoId(savedVideo.getVideoId());
		
		return response;
	}

	public String uploadThumbnail(MultipartFile file, String videoId) throws VideoException {
		Video video = getVideoById(videoId);
		String url = s3Service.uploadFile(file);
		video.setThumbnailUrl(url);
		videoRepo.save(video);
		return url;
	}

	public Video updateDetails(Video video,String id) throws VideoException {
		Video updatedVideo = getVideoById(id);
		updatedVideo.setTitle(video.getTitle());
		updatedVideo.setDescription(video.getDescription());
		updatedVideo.setThumbnail(video.getThumbnail());
		updatedVideo.setTags(video.getTags());
		updatedVideo.setUpdatedAt(LocalDateTime.now());
		updatedVideo.setVideoStatus(video.getVideoStatus());
		
		
		return videoRepo.save(updatedVideo);
	}

	public List<Video> getVideos() {
		return videoRepo.findAll();
	}

	public Video getVideoById(String id) throws VideoException {
		return videoRepo.findById(id).orElseThrow(()-> new VideoException("Video with id :"+id+" not found"));
	}


	public List<Video> getVideoOfUser(String email) throws VideoException, UserException {
		User user = userService.getUserByEmail(email);
		List<Video> videos = new ArrayList<>();
		for(String videoId: user.getVideos()) {
			Video video = getVideoById(videoId);
			videos.add(video);
		}
		return videos;
	}
	
	
	//also remove the  comments
	public String deleteVideo(String id,String token) throws VideoException, CommentException {
		User user =userService.getProfile(token); 
		Video video = getVideoById(id);
		if(user.getEmail().equals(video.getUser())) {
	    	videoRepo.deleteById(id);
	    	
	    	 List<String> videos = user.getVideos();
	         if (videos != null && videos.contains(id)) {
	             videos.remove(id);
	             user.setVideos(videos); 
	             userService.updateUser(user); 
	         }
	         
//	         List<String> comments = video.getComments();
//	         for(String cId: comments) {
//	        	 commentService.deleteComment(cId);
//	         }
	    	
	    	return "Video with id: " + id + " has been deleted successfully.";	    	
	    }
	    else {
	    	throw new VideoException("You are not authorized to delete this video");
	    }
	}
	
	public Video updateVideo(Video video) {
		return videoRepo.save(video);
	}

}
