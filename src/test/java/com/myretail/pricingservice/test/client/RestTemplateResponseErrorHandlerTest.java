package com.myretail.pricingservice.test.client;

import javax.ws.rs.NotFoundException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.client.RestTemplateResponseErrorHandler;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.exception.ExternalCommsException;
import com.myretail.pricingservice.test.UnitTest;

/*
 * Test suite for the RestTemplateResponseErrorHandler.java which is the configuration to handle exceptions when
 * consuming the external API
 */
@Category(UnitTest.class)
@RunWith(SpringRunner.class)
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
 
    /*
     * The @Test annotation here says, what exception should be expected from the custom RestTemplateResponseErrorHandler
     * When a certain HTTP response is got from the server
     * For example, when server throws a 404, we verify that our custom error handler responds with a NotFoundException
     */
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
    
    /*
     * When the mock server returns any 500x error, our custom error handler responds with a ExternalCommsException
     */
    @Test(expected = ExternalCommsException.class)
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
    
    /*
     * When the mock server returns any 400x error (except 404), our custom error handler responds with a ExternalCommsException
     */
    @Test(expected = ExternalCommsException.class)
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