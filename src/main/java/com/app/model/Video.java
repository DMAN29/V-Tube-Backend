package com.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="Videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

	@Id
	private String videoId;
	private String user;	// User Email who post it
	private String title;
	private String description;
	private String videoUrl;
	private String thumbnail;
	private String thumbnailUrl;
	private VideoStatus videoStatus;
	private Set<String> tags=new HashSet<>();
	private Integer likes;
	private Integer dislikes;
	private Integer views;
	private LocalDateTime uploadAt;
	private LocalDateTime updatedAt;
	private List<String> comments = new ArrayList<>();
	
}
