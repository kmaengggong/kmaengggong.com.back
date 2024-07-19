package com.kmaengggong.kmaengggong.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kmaengggong.kmaengggong.common.exception.AlreadyExistsException;
import com.kmaengggong.kmaengggong.common.exception.AlreadyLikedException;
import com.kmaengggong.kmaengggong.common.exception.PasswordMismatchException;
import com.kmaengggong.kmaengggong.common.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AlreadyLikedException.class)
	public ResponseEntity<?> alreadyLikedException(AlreadyLikedException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<?> alreadyExistsException(AlreadyExistsException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<?> passwordMismatchException(PasswordMismatchException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
