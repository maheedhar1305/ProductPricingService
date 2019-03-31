package com.myretail.pricingservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myretail.pricingservice.client.ProductServiceClient;
import com.myretail.pricingservice.dao.PriceRepository;
import com.myretail.pricingservice.domain.Description;
import com.myretail.pricingservice.domain.InventoryInfo;
import com.myretail.pricingservice.domain.Item;
import com.myretail.pricingservice.domain.Price;
import com.myretail.pricingservice.domain.PricingInfo;
import com.myretail.pricingservice.domain.Product;
import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.exception.ExceptionUtil;
import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.ServerSideException;
import com.rusapi.candigram.domain.Authentication;

@Component
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private ProductServiceClient productServiceClient;

	@Override
	public ProductPricingInfo getPriceInfoForProduct(String productId)
			throws NotFoundException,ServerSideException,InternalServiceException
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
		catch (NotFoundException | ServerSideException t) {
			LOGGER.error("Exception! getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw t;
		}
		catch (Throwable t) {
			LOGGER.error("Exception! getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw new InternalServiceException(t.getMessage());
		}
	}

	@Override
	public void savePriceForProduct(ProductPricingInfo info)
			throws InternalServiceException, BadRequestException
	{
		try {
			validate(info);
			
			Price price = new Price();
			price.setProductId(info.getId());
			price.setCurrencyCode(info.getCurrent_price().getCurrency_code());
			price.setCurrentPrice(info.getCurrent_price().getValue());
			price.setLastModified(new Date());
			
			if (priceRepository.doesProductExists(info.getId())) {
				priceRepository.updateSalaryInfo(price);
			} 
			else {
				priceRepository.save(price);
			}
		}
		catch (BadRequestException t) {
			LOGGER.error("Exception! savePriceForProduct for " + info + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw t;
		}
		catch (Throwable t) {
			LOGGER.error("Exception! savePriceForProduct for " + info + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw t;
		}
	}
	
	private void validate (ProductPricingInfo info) throws BadRequestException
	{
		StringBuilder msg = new StringBuilder();
		if (info == null) {
			msgAppend(msg, "PricingInfo is missing");
		}
		else {
			if (info.getId() == null || info.getId().trim().isEmpty()) {
				msgAppend(msg, "Product id is missing");
			}
			if (info.getCurrent_price() == null) {
				msgAppend(msg, "Pricing info is missing");
			}
			else {
				if (info.getCurrent_price().getCurrency_code() == null || info.getCurrent_price().getCurrency_code().trim().isEmpty()) {
					msgAppend(msg, "Currency code is missing");
				}
				if (info.getCurrent_price().getValue() == null) {
					msgAppend(msg, "Price is missing");
				} 
				else if (info.getCurrent_price().getValue() <= 0) {
					msgAppend(msg, "Price must be greater than zero");
				}
			}
		}
		if (msg.length() > 0) {
			msg.insert(0, "One or more of the following issues have been found: ");
		}
		if (msg.length() > 0) {
			throw new BadRequestException(msg.toString());
		}
	}
	
	private void msgAppend(StringBuilder sb, String msg) {
		sb.append((sb.length() > 0) ? ", " : "");
		sb.append(msg);
	}
}
