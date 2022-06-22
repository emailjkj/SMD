package com.smd.bookstore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.smd.bookstore.datatransferobject.ErrorDTO;

@RestControllerAdvice
public class CustomExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(CustomExceptionHandler.class);
	private static final String ERROR_OCCURED = "Error occured: {}";

	@ExceptionHandler(value = { DuplicateEntityException.class, ConstraintsViolationException.class })
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public ErrorDTO duplicateEntityExceptionHandler(Exception exception) {
		LOG.error(ERROR_OCCURED, exception.getMessage());
		return new ErrorDTO(409, exception.getMessage());
	}

	@ExceptionHandler(value = EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorDTO entityNotFoundExceptionHandler(Exception exception) {
		LOG.error(ERROR_OCCURED, exception.getMessage());
		return new ErrorDTO(404, exception.getMessage());
	}

	@ExceptionHandler(value = { InternalServerError.class, Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDTO genericExceptionHandler(Exception exception) {
		LOG.error(ERROR_OCCURED, exception);
		return new ErrorDTO(500, "Something went wrong. Please contact administrator.");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO validationExceptionHandler(MethodArgumentNotValidException exception) {
		LOG.error(ERROR_OCCURED, exception);
		return new ErrorDTO(400, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, InvalidFormatException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO validationExceptionHandler(HttpMessageNotReadableException exception) {
		LOG.error(ERROR_OCCURED, exception);
		return new ErrorDTO(400, exception.getLocalizedMessage());
	}

}
