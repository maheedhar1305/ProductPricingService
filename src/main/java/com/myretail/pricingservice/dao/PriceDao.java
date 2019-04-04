package com.myretail.pricingservice.dao;

import com.myretail.pricingservice.domain.Price;

/*
 * Declaration of custom DAO methods required by the service
 */
public interface PriceDao {
	public void updatePriceInfo(Price price);
	public Boolean doesProductExists(String productId);	
}
