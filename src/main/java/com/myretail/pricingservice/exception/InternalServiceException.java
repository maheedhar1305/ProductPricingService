package com.myretail.pricingservice.exception;

/*
 * Exception indicating an unknown internal issue in this service
 */
public class InternalServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InternalServiceException() {
		super();
	}

	public InternalServiceException(String message) {
		super(message);
	}
	
	public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
