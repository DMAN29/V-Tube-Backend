package com.app.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
public class S3Service implements FileService{

	public static final String BUCKET = "video-stream-tube";
	
	@Autowired
	private AmazonS3 amazonS3Client;
	
	@Override
	public String uploadFile(MultipartFile file) {
		var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
		var key = UUID.randomUUID().toString()+ "." +filenameExtension;
		var metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		
		try {
			amazonS3Client.putObject(BUCKET,key,file.getInputStream(),metadata);
		} 
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An exception Occured while uploading the file");
		}
		
		amazonS3Client.setObjectAcl(BUCKET,key,CannedAccessControlList.PublicRead);
		
		return amazonS3Client.getUrl(BUCKET, key).toString();		
	}

}
