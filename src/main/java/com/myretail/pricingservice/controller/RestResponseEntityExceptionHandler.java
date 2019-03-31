package com.myretail.pricingservice.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class RestResponseEntityExceptionHandler {
  //extends ResponseEntityExceptionHandler {
 
//    @ExceptionHandler(value 
//      = { InternalServiceException.class, IllegalStateException.class })
//    protected ResponseEntity<Object> handleConflict(
//      RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse, 
//          new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
}
