package com.myretail.pricingservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.properties.ProductServiceClientProperties;

/*
 * Implementation of ProductServiceClient, that consumes an external endpoint hosted at redsky.target.com
 */
@Component
public class ProductServiceClientImpl implements ProductServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceClientImpl.class);
	
	@Autowired
	ProductServiceClientProperties properties;
	
	/*
	 * Custom template (dependency) defined in RestTemplateConfig.java will be injected into this bean
	 */
	@Autowired
	RestTemplate restTemplate;

	/*
	 * Retrieve the product information from the external api
	 * @param productId the id of the product whose information to retrieve
	 * @return InventoryInfo information about the product
	 */
	@Override
	public InventoryInfo getProductInfo(String productId)
	{	
		InventoryInfo inventoryInfo = restTemplate.getForObject(properties.getUrl(), InventoryInfo.class, productId);
			
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Inventory information for product " + productId + "is = " + inventoryInfo);
		}
		
		return inventoryInfo;
	}
}
