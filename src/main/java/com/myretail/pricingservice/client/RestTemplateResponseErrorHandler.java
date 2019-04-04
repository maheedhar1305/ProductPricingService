package com.myretail.pricingservice.client;

import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.myretail.pricingservice.exception.ExternalCommsException;

/*
 * Error handler defining how to handle exceptions thrown by external APIs being consumed by the service
 */
@Component
public class RestTemplateResponseErrorHandler 
  implements ResponseErrorHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);
 
	/*
	 * Defines what http response from the external API constitutes an error 
	 * @see org.springframework.web.client.ResponseErrorHandler#hasError(org.springframework.http.client.ClientHttpResponse)
	 */
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) 
      throws IOException {
        return (
          httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR 
          || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }
 
    /*
     * Defines how our service should handle error responses from the external API
     * @see org.springframework.web.client.ResponseErrorHandler#handleError(org.springframework.http.client.ClientHttpResponse)
     */
    @Override
    public void handleError(ClientHttpResponse httpResponse) 
      throws IOException
    {
    	LOGGER.error("Error in consuming api : " + httpResponse.getRawStatusCode()+ ", reason : " + httpResponse.getStatusText());	
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
        	throw new ExternalCommsException(httpResponse.getStatusText());
        } 
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
        	if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(httpResponse.getStatusText());
            }
        	throw new ExternalCommsException(httpResponse.getStatusText());    
        }
    }
}
