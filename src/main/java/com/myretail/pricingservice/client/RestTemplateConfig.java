package com.myretail.pricingservice.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.properties.ProductServiceClientProperties;

@Configuration
public class RestTemplateConfig {
	
	@Autowired
	ProductServiceClientProperties properties;
	
	@Bean
	public ClientHttpRequestFactory getClientHttpRequestFactory() {
	      RequestConfig config = RequestConfig.custom()
	        .setConnectTimeout(properties.getTimeout())
	        .setConnectionRequestTimeout(properties.getTimeout())
	        .setSocketTimeout(properties.getTimeout())
	        .build();
	      CloseableHttpClient client = HttpClientBuilder
	        .create()
	        .setDefaultRequestConfig(config)
	        .build();
	      return new HttpComponentsClientHttpRequestFactory(client);
	}
	
	@Bean
	public RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
	    RestTemplate restTemplate = new RestTemplate(factory);
	    //TODO
	    //TODO actually the client throws 404 when not found, need to handle all that
// https://www.baeldung.com/spring-rest-template-error-handling
//	    restTemplate.setErrorHandler(new RestResponseErrorHandler());
	    return restTemplate;
	}

}
