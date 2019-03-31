package com.myretail.pricingservice.dao;

import com.myretail.pricingservice.domain.Price;

public interface PriceDao {
	public void updateSalaryInfo(Price price);
	public Boolean doesProductExists(String productId);	
}
