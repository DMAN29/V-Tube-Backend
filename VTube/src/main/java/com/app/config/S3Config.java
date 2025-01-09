package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Bean
	public AmazonS3 amazonS3() {
	    String accessKey = System.getProperty("cloud.aws.credentials.access-key");
	    String secretKey = System.getProperty("cloud.aws.credentials.secret-key");
	   
	    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	    return AmazonS3ClientBuilder.standard()
	            .withRegion("ap-southeast-2")
	            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	            .build();
	}
}
