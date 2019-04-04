package com.myretail.pricingservice.exception;

/*
 * Indicates that the requested entity cannot be found
 */
public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
	
	public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
