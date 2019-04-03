package com.myretail.pricingservice.client;

import javax.ws.rs.NotFoundException;

import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.exception.ExternalCommsException;

/*
 * Declaration of the necessary client methods to communicate to an external source to retrieve product information
 */
public interface ProductServiceClient {
	public InventoryInfo getProductInfo(String productId) throws ExternalCommsException, NotFoundException;
}
