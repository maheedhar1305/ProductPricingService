package com.myretail.pricingservice.client;

import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.myretail.pricingservice.exception.ServerSideException;

@Component
public class RestTemplateResponseErrorHandler 
  implements ResponseErrorHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);
 
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) 
      throws IOException {
        return (
          httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR 
          || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }
 
    @Override
    public void handleError(ClientHttpResponse httpResponse) 
      throws IOException
    {
    	LOGGER.error("Error in consuming api : " + httpResponse.getRawStatusCode()+ ", reason : " + httpResponse.getStatusText());	
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
        	throw new ServerSideException(httpResponse.getStatusText());
        } 
        else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
        	if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException();
            }
        	throw new ServerSideException(httpResponse.getStatusText());    
        }
    }
}
