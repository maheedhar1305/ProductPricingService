package com.myretail.pricingservice.domain;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * A data-type to help provide meaningful error messages to the consumer of the API
 */
public class ApiError {
   private HttpStatus status;
	  
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
   private LocalDateTime timestamp;
   private String message;
   private String debugMessage;

   public ApiError() {
       timestamp = LocalDateTime.now();
   }

   public ApiError(HttpStatus status, String message) {
       this();
       this.setStatus(status);
   }

   public ApiError(HttpStatus status, Throwable ex) {
       this();
       this.setStatus(status);
       this.setMessage("Unexpected error");
       this.setDebugMessage(ex.getLocalizedMessage());
   }

   public ApiError(HttpStatus status, String message, Throwable ex) {
       this();
       this.setStatus(status);
       this.setMessage(message);
       this.setDebugMessage(ex.getLocalizedMessage());
   }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
