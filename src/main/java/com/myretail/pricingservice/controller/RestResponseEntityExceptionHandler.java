package com.myretail.pricingservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.InvalidDataException;
import com.myretail.pricingservice.exception.EntityNotFoundException;
import com.myretail.pricingservice.exception.ExternalCommsException;
import com.myretail.pricingservice.domain.ApiError;

/*
 * A custom implementation of ResponseEntityExceptionHandler that provides meaningful error messages 
 * to the consumer of the API, when there are issues
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
 
	/*
	 * Handle all internal exceptions that constitute a HTTP 500 Internal server error response from the API
	 */
    @ExceptionHandler(value 
      = { InternalServiceException.class, ExternalCommsException.class, Throwable.class })
    protected ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request)
    {
    	String error = "Internal servor error";
    	return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }
    
    /*
	 * Handle exceptions that constitute a HTTP 404 Resource not found response from the API
	 */
    @ExceptionHandler(value 
      = { EntityNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request)
    {
    	String error = "Entity not found";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, error, ex));
    }
    
    /*
	 * Handle exceptions that constitute a HTTP 400 Bad request response from the API
	 */
    @ExceptionHandler(value 
    	      = { InvalidDataException.class })
    protected ResponseEntity<Object> handleNotFound(InvalidDataException ex, WebRequest request)
    {
    	String error = "Invalid JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
    
    /*
     * Send a JSON response on errors, that clients can use to troubleshoot
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("API Error : " + apiError);
    	}
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
