package com.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<String> handleUserException(UserException ex) {
	    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(VideoException.class)
	public ResponseEntity<String> handleVideoException(VideoException ex) {
	    return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CommentException.class)
	public ResponseEntity<String> handleCommentException(CommentException ex) {
	    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
            "{\"error\": \"Internal Server Error\", \"message\": \"" + ex.getMessage() + "\"}",
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}