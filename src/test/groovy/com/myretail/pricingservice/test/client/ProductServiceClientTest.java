package com.myretail.pricingservice.test.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.client.MockRestServiceServer;
import com.myretail.pricingservice.properties.ProductServiceClientProperties;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.pricingservice.client.ProductServiceClient;
import com.myretail.pricingservice.client.ProductServiceClientImpl;
import com.myretail.pricingservice.domain.Description;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.domain.Item;
import com.myretail.pricingservice.domain.Product;

@RunWith(SpringRunner.class)
@RestClientTest(ProductServiceClientImpl.class)
public class ProductServiceClientTest {
 
    @Autowired
    private ProductServiceClient client;
 
    @Autowired
    private MockRestServiceServer server;
 
    @Autowired
    private ObjectMapper objectMapper;
    
    @TestConfiguration
    static class SomeConfigFooBarBuzz {
    	@Bean
    	public ClientHttpRequestFactory getClientHttpRequestFactory() {
    		int timeout = 5000;
    	      RequestConfig config = RequestConfig.custom()
    	        .setConnectTimeout(timeout)
    	        .setConnectionRequestTimeout(timeout)
    	        .setSocketTimeout(timeout)
    	        .build();
    	      CloseableHttpClient client = HttpClientBuilder
    	        .create()
    	        .setDefaultRequestConfig(config)
    	        .build();
    	      return new HttpComponentsClientHttpRequestFactory(client);
    	}
    	
    	@Bean
    	public ProductServiceClientProperties getProductServiceClientProperties() {
    		 ProductServiceClientProperties props = new ProductServiceClientProperties();
    		 props.setUrl("https://redsky.target.com/v2/pdp/tcin/{productId}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");
    	     return props;
    	}
    }

 
    @Before
    public void setUp() throws Exception {
    	InventoryInfo inventoryInfo = new InventoryInfo();
    	Product product = new Product();
    	Item item = new Item();
    	Description desc = new Description();
    	desc.setTitle("product");
    	item.setProduct_description(desc);
    	product.setItem(item);
    	inventoryInfo.setProduct(product);
    	
        String detailsString = 
          objectMapper.writeValueAsString(inventoryInfo);
         
        this.server.expect(requestTo("https://redsky.target.com/v2/pdp/tcin/john?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"))
          .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }
 
    @Test
    public void whenCallingGetUserDetails_thenClientMakesCorrectCall() 
      throws Exception {
 
    	InventoryInfo details = this.client.getProductInfo("john");
    	Assert.assertEquals(details.getProduct().getItem().getProduct_description().getTitle(), "product");
    }
}
