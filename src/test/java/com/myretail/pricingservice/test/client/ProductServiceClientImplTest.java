package com.myretail.pricingservice.test.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.client.ProductServiceClient;
import com.myretail.pricingservice.client.ProductServiceClientImpl;
import com.myretail.pricingservice.domain.Description;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.domain.Item;
import com.myretail.pricingservice.domain.Product;
import com.myretail.pricingservice.exception.ExternalCommsException;
import com.myretail.pricingservice.exception.EntityNotFoundException;
import com.myretail.pricingservice.properties.ProductServiceClientProperties;
import com.myretail.pricingservice.test.UnitTest;

/*
 * Happy path testing of the client consuming external API.
 * The testing of error handler is in the RestTemplateResponseErrorHandlerTest.java file
 */
@Category(UnitTest.class)
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceClientImplTest {
 
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    ProductServiceClientProperties properties;
	 
    @InjectMocks
    private ProductServiceClient productServiceClient = new ProductServiceClientImpl();
    
    @Test
    public void givenMockExternalServer_whenClientToExternalApiIsCalled_shouldReturnMockedObject() throws EntityNotFoundException, ExternalCommsException {
    	// given
    	// mocked object
    	InventoryInfo inventoryInfo = new InventoryInfo();
    	Product product = new Product();
    	Item item = new Item();
    	Description desc = new Description();
    	desc.setTitle("product");
    	item.setProduct_description(desc);
    	product.setItem(item);
    	inventoryInfo.setProduct(product);
    	
    	String url = 
    	"https://redsky.target.com/v2/pdp/tcin/{productId}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
    	
    	Mockito.when(properties.getUrl()).thenReturn(url);
    	
    	// mock server that returns the mocked object when called
        Mockito
          .when(restTemplate.getForObject(url ,InventoryInfo.class, "john"))
          .thenReturn(inventoryInfo);
	 
        // when 
        InventoryInfo details = this.productServiceClient.getProductInfo("john");
        
        // then
    	Assert.assertEquals(details.getProduct().getItem().getProduct_description().getTitle(), "product");
    }
}
