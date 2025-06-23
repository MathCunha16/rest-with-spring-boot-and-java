package com.github.MathCunha16.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}
