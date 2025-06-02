package com.github.MathCunha16.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial") // não é necessario usar o Serializable
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationException extends RuntimeException {
	public UnsupportedMathOperationException(String message) {
		super(message);
	}
}
