package com.store.electronicsstore.service.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -3386564163109156364L;

	public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
