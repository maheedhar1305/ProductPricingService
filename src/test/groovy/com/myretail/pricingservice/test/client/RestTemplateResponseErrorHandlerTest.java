package com.myretail.pricingservice.test.client;

import javax.ws.rs.NotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.client.RestTemplateResponseErrorHandler;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.exception.ServerSideException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { NotFoundException.class, InventoryInfo.class, ServerSideException.class })
@RestClientTest
public class RestTemplateResponseErrorHandlerTest {
 
    @Autowired
    private RestTemplateBuilder builder;
    
    private RestTemplate restTemplate;
    
    private MockRestServiceServer server;
 
    @Before
    public void setUp() {
    	restTemplate = this.builder
    	          .errorHandler(new RestTemplateResponseErrorHandler())
    	          .build();
    	 
    	server = MockRestServiceServer.createServer(restTemplate);
    }
 
    @Test(expected = NotFoundException.class)
    public void  givenRemoteApiCall_when404Error_thenThrowNotFound() {
        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);
 
        this.server
          .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/bars/4242"))
          .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
          .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
 
        restTemplate.getForObject("/bars/4242", InventoryInfo.class);
        this.server.verify();
    }
    
    @Test(expected = ServerSideException.class)
    public void  givenRemoteApiCall_when500Error_thenThrowServerSideException() {
        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);
 
       this.server
          .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/bars/4243"))
          .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
          .andRespond(MockRestResponseCreators.withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
 
        restTemplate.getForObject("/bars/4243", InventoryInfo.class);
        this.server.verify();
    }
    
    @Test(expected = ServerSideException.class)
    public void  givenRemoteApiCall_when400xError_thenThrowServerSideException() {
        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);
 
       this.server
          .expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("/bars/4244"))
          .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
          .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST));
 
        restTemplate.getForObject("/bars/4244", InventoryInfo.class);
        this.server.verify();
    }
}