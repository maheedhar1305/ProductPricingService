package com.myretail.pricingservice.client;

import javax.ws.rs.NotFoundException;

import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.exception.ServerSideException;

public interface ProductServiceClient {
	public InventoryInfo getProductInfo(String productId) throws ServerSideException, NotFoundException;
}
