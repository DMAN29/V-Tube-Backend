package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.CommentException;
import com.app.exception.UserException;
import com.app.exception.VideoException;
import com.app.model.Video;
import com.app.response.VideoResponse;
import com.app.service.VideoService;

@RestController
@RequestMapping("/video")
public class VideoController {

	@Autowired
	private VideoService videoService;
	
	@PostMapping("/upload-video")
	public ResponseEntity<VideoResponse> uploadVideo(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token){
		return new ResponseEntity<VideoResponse>(videoService.uploadVideo(file,token),HttpStatus.CREATED);
	}
	
	@PostMapping("/upload-thumbnail")
	public ResponseEntity<String> uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) throws VideoException{
		return new ResponseEntity<String>(videoService.uploadThumbnail(file,videoId),HttpStatus.CREATED);
	}
	
	@PutMapping("/update-video/{id}")
	public ResponseEntity<Video> updateVideo(@RequestBody Video video,@PathVariable("id") String videoId) throws VideoException{
		return new ResponseEntity<Video>(videoService.updateDetails(video,videoId),HttpStatus.ACCEPTED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Video>> allVideos(){
		return new ResponseEntity<List<Video>>(videoService.getVideos(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Video> getVideo(@PathVariable("id") String id) throws VideoException{
		return new ResponseEntity<Video>(videoService.getVideoById(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteVideo(@PathVariable("id") String id,@RequestHeader("Authorization")String token) throws VideoException, CommentException{
		return new ResponseEntity<String>(videoService.deleteVideo(id,token),HttpStatus.OK);
	}
	
	@GetMapping("/user/{email}")
	public ResponseEntity<List<Video>> getVideosOfUser(@PathVariable("email") String email) throws VideoException, UserException{
		return new ResponseEntity<List<Video>>(videoService.getVideoOfUser(email),HttpStatus.OK);
	}

}
