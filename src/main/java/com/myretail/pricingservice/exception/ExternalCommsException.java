package com.myretail.pricingservice.exception;

/*
 * Indicates an error in getting the information from an external api
 */
public class ExternalCommsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ExternalCommsException() {
		super();
	}

	public ExternalCommsException(String message) {
		super(message);
	}
	
	public ExternalCommsException(String message, Throwable cause) {
        super(message, cause);
    }
}
