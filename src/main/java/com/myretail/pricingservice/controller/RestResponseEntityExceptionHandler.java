package com.myretail.pricingservice.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.ServerSideException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
 
    @ExceptionHandler(value 
      = { InternalServiceException.class, ServerSideException.class })
    protected ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request)
    {
    	//LOGGER.error("Exception! PricingServiceImpl getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
        String bodyOfResponse = "Internal servor error. Problem identifier : " + ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, 
          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    @ExceptionHandler(value 
      = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request)
    {
    	//LOGGER.error("Exception! PricingServiceImpl getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
    	String bodyOfResponse = "Product not found : " + ex.getMessage();
    	return handleExceptionInternal(ex, bodyOfResponse, 
    	          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value 
    	      = { BadRequestException.class })
    	    protected ResponseEntity<Object> handleNotFound(BadRequestException ex, WebRequest request)
    	    {
    	    	//LOGGER.error("Exception! PricingServiceImpl getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
    	    	String bodyOfResponse = "Bad request : " + ex.getMessage();
    	    	return handleExceptionInternal(ex, bodyOfResponse, 
    	    	          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    	    }
}
