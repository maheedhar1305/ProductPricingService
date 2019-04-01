package com.myretail.pricingservice.exception;

/*
 * Indicates an error in getting the information from an external api
 */
public class ServerSideException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ServerSideException() {
		super();
	}

	public ServerSideException(String message) {
		super(message);
	}
}
