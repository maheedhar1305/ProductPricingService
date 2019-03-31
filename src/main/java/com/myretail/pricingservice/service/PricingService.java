package com.myretail.pricingservice.service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.ServerSideException;

public interface PricingService {
	public ProductPricingInfo getPriceInfoForProduct(String productId) 
			throws NotFoundException,ServerSideException,InternalServiceException;
	
	public void savePriceForProduct(String productId, ProductPricingInfo info)
			throws InternalServiceException, BadRequestException;
}
