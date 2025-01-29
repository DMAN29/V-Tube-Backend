package com.app.model;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	@Id
	private String cId;
	private String user;
	private String videoId;
	private String message;
	
}
