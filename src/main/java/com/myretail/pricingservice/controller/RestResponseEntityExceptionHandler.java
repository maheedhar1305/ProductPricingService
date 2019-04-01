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
import com.myretail.pricingservice.exception.ServerSideException;
import com.myretail.pricingservice.domain.ApiError;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
 
    @ExceptionHandler(value 
      = { InternalServiceException.class, ServerSideException.class, Throwable.class })
    protected ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request)
    {
    	String error = "Internal servor error";
    	return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }
    
    @ExceptionHandler(value 
      = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request)
    {
    	String error = "No matching data found for given parameters";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, error));
    }
    
    @ExceptionHandler(value 
    	      = { BadRequestException.class })
    protected ResponseEntity<Object> handleNotFound(BadRequestException ex, WebRequest request)
    {
    	String error = "Invalid JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("API Error : " + apiError);
    	}
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
