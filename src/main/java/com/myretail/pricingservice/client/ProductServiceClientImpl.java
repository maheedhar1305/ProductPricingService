package com.myretail.pricingservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.properties.ProductServiceClientProperties;

@Component
public class ProductServiceClientImpl implements ProductServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceClientImpl.class);
	
	@Autowired
	ClientHttpRequestFactory clientHttpRequestFactory;
	
	@Autowired
	ProductServiceClientProperties properties;

	@Override
	public InventoryInfo getProductInfo(String productId) {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		InventoryInfo inventoryInfo = restTemplate.getForObject(properties.getUrl(), InventoryInfo.class, productId);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inventory information for product " + productId + "is = " + inventoryInfo);
		}
		
		return inventoryInfo;
	}

}
