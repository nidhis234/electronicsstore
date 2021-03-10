package com.store.electronicsstore.service.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Exceptionhelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(Exceptionhelper.class);

	@ExceptionHandler(value = { ProductAlreadyExistsException.class })
	public ResponseEntity<ExceptionResponse> handleDuplicateProduct(ProductAlreadyExistsException ex) {
		LOGGER.error("Duplicate Input Exception");

		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(ex.getMessage()),
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(value = { ProductNotFoundException.class })
	public Object handleBadUpdateRequest(ProductNotFoundException ex) {
		LOGGER.error("Id not present in DB Exception: ", ex.getMessage());

		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { ProductsNotPresentException.class })
	public Object handleNoProductsFoundRequest(ProductsNotPresentException ex) {
		LOGGER.error("No products found Exception: ", ex.getMessage());

		return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
	}

}
