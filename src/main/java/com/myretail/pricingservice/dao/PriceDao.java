package com.myretail.pricingservice.dao;

import com.myretail.pricingservice.domain.Price;

public interface PriceDao {
	public void updatePriceInfo(Price price);
	public Boolean doesProductExists(String productId);	
}
