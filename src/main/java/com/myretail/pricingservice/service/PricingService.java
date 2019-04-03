package com.myretail.pricingservice.service;

import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.InvalidDataException;
import com.myretail.pricingservice.exception.EntityNotFoundException;
import com.myretail.pricingservice.exception.ExternalCommsException;

/*
 * Declaring the operations that will be performed by the service
 */
public interface PricingService {
	public ProductPricingInfo getPriceInfoForProduct(String productId) 
			throws EntityNotFoundException,ExternalCommsException,InternalServiceException;
	
	public void savePriceForProduct(String productId, ProductPricingInfo info)
			throws InternalServiceException, InvalidDataException;
}
