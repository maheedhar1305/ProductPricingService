package com.myretail.pricingservice.service;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myretail.pricingservice.ExceptionUtil;
import com.myretail.pricingservice.InternalServiceException;
import com.myretail.pricingservice.client.ProductServiceClient;
import com.myretail.pricingservice.dao.PriceRepository;
import com.myretail.pricingservice.domain.Description;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.domain.Item;
import com.myretail.pricingservice.domain.Price;
import com.myretail.pricingservice.domain.PricingInfo;
import com.myretail.pricingservice.domain.Product;
import com.myretail.pricingservice.domain.ProductPricingInfo;

@Component
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private ProductServiceClient productServiceClient;

	@Override
	public ProductPricingInfo getPriceInfoForProduct(String productId)
	throws Throwable 
	{
		try {
			InventoryInfo info = productServiceClient.getProductInfo(productId);
			Price price = priceRepository.findByProductId(productId);
			
			// Throw a NotFoundException if mongo has no data for the product
			Optional.ofNullable(price).orElseThrow(NotFoundException::new);
			
			ProductPricingInfo result = new ProductPricingInfo();
			result.setId(productId);
			result.setName(Optional.ofNullable(info)
									.map(InventoryInfo::getProduct)
									.map(Product::getItem)
									.map(Item::getProduct_description)
									.map(Description::getTitle)
									.orElse(null));
			
			PricingInfo current_price = new PricingInfo();
			current_price.setCurrency_code(Optional.ofNullable(price.getCurrencyCode())
													.orElse("USD"));
			current_price.setValue(price.getCurrentPrice());
			result.setCurrent_price(current_price);
			
			return result;
		}
		catch (Throwable t) {
			LOGGER.error("Exception! PricingServiceImpl getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw new InternalServiceException(t.getMessage());
		}
	}

	@Override
	public void savePriceForProduct(ProductPricingInfo info) {
		// TODO validate that there is data in the price field before you put it in

	}
}
