package com.myretail.pricingservice.service;

import com.myretail.pricingservice.domain.ProductPricingInfo;

public interface PricingService {
	public ProductPricingInfo getPriceInfoForProduct(String productId) throws Throwable;
	public void savePriceForProduct(ProductPricingInfo info);
}
