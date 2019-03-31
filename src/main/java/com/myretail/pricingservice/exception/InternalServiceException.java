package com.myretail.pricingservice.exception;

/*
 * Exception thrown by this pricing service
 */
public class InternalServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InternalServiceException() {
		super();
	}

	public InternalServiceException(String message) {
		super(message);
	}
}
