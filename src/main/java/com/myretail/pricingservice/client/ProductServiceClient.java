package com.myretail.pricingservice.client;

import com.myretail.pricingservice.domain.InventoryInfo;

public interface ProductServiceClient {
	public InventoryInfo getProductInfo(String productId) throws Throwable;
}
