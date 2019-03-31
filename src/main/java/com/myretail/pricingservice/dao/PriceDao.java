package com.myretail.pricingservice.dao;

import com.myretail.pricingservice.domain.Price;
import com.myretail.pricingservice.domain.PricingInfo;

public interface PriceDao {
	public PricingInfo findCurrentPriceForProduct(String productId);
	public Boolean productDataExists(String productId);
	public void updatePricesForProduct(String productId, PricingInfo info);
	public void save(Price data);
}