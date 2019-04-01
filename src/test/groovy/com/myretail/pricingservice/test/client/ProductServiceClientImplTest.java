package com.myretail.pricingservice.test.client;

import javax.ws.rs.NotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.client.ProductServiceClient;
import com.myretail.pricingservice.client.ProductServiceClientImpl;
import com.myretail.pricingservice.domain.Description;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.domain.Item;
import com.myretail.pricingservice.domain.Product;
import com.myretail.pricingservice.exception.ServerSideException;
import com.myretail.pricingservice.properties.ProductServiceClientProperties;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceClientImplTest {
 
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    ProductServiceClientProperties properties;
	 
    @InjectMocks
    private ProductServiceClient productServiceClient = new ProductServiceClientImpl();
    
    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() throws NotFoundException, ServerSideException {
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
    	
        Mockito
          .when(restTemplate.getForObject(url ,InventoryInfo.class, "john"))
          .thenReturn(inventoryInfo);
	 
        InventoryInfo details = this.productServiceClient.getProductInfo("john");
    	Assert.assertEquals(details.getProduct().getItem().getProduct_description().getTitle(), "product");
    }
}
