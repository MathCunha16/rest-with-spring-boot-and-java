package com.github.MathCunha16.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.MathCunha16.exceptions.ExceptionResponse;
import com.github.MathCunha16.exceptions.UnsupportedMathOperationException;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		ExceptionResponse Response = new ExceptionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
				);
		
		return new ResponseEntity<>(Response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(UnsupportedMathOperationException.class)
	public final ResponseEntity<ExceptionResponse> handleBadExceptions(Exception ex, WebRequest request) {
		ExceptionResponse Response = new ExceptionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false)
				);
		
		return new ResponseEntity<>(Response, HttpStatus.BAD_REQUEST);
		
	}

}
