package com.myretail.pricingservice.service;

import java.util.Date;
import java.util.Optional;

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
import com.myretail.pricingservice.exception.EntityNotFoundException;
import com.myretail.pricingservice.exception.ExceptionUtil;
import com.myretail.pricingservice.exception.InternalServiceException;
import com.myretail.pricingservice.exception.InvalidDataException;
import com.myretail.pricingservice.exception.ExternalCommsException;

/*
 * Implementation of the PricingService definition
 */
@Component
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private ProductServiceClient productServiceClient;

	/*
	 * The service that provides information about a product's name and price
	 * Aggregates data from an external API and also an internal data store to acquire all the information
	 * @param productId the id of the product whose information to retrieve
	 * @return ProductPricingInfo if valid information is available in the multiple sources of data
	 * @throws EntityNotFoundException if data is unavailable in any of the sources
	 * @throws ExternalCommsException if there were issues in communicating with the external source
	 * @throws InternalServiceException if there were any issues in processing the request by the service.
	 */
	@Override
	public ProductPricingInfo getPriceInfoForProduct(String productId)
			throws EntityNotFoundException,ExternalCommsException,InternalServiceException
	{
		try {
			InventoryInfo info = productServiceClient.getProductInfo(productId);
			Price price = priceRepository.findByProductId(productId);
			
			// Throw a EntityNotFoundException if mongo has no data for the product
			Optional.ofNullable(price).orElseThrow(() -> new EntityNotFoundException("Entity does not exist in the internal database"));
			
			ProductPricingInfo result = new ProductPricingInfo();
			result.setId(productId);
			result.setName(Optional.ofNullable(info)
									.map(InventoryInfo::getProduct)
									.map(Product::getItem)
									.map(Item::getProduct_description)
									.map(Description::getTitle)
									.orElse(null));
			
			PricingInfo current_price = new PricingInfo();
			current_price.setCurrency_code(price.getCurrencyCode());
			current_price.setValue(price.getCurrentPrice());
			result.setCurrent_price(current_price);
			
			return result;
		}
		catch (NotFoundException | EntityNotFoundException t) {
			LOGGER.error("Exception! getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw new EntityNotFoundException("No product/price information found for id : "+ productId);
		}
		catch (ExternalCommsException t) {
			LOGGER.error("Exception! getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw t;
		}
		catch (Throwable t) {
			LOGGER.error("Exception! getPriceInfoForProduct for " + productId + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw new InternalServiceException(t.getMessage(), t);
		}
	}

	/*
	 * The service that upserts (insert/update) information about a product's price
	 * Creates a new record if the product is new, Updates an existing record if a record for the product is already available
	 * @param ProductPricingInfo the price information for a product to be persisted to the database
	 * @throws InvalidDataException if the data provided to the service was invalid
	 * @throws InternalServiceException if there were any issues in processing the request by the service.
	 */
	@Override
	public void savePriceForProduct(String productId, ProductPricingInfo info)
			throws InternalServiceException, InvalidDataException
	{
		try {
			validate(productId, info);
			
			Price price = new Price();
			price.setProductId(info.getId());
			price.setCurrencyCode(info.getCurrent_price().getCurrency_code());
			price.setCurrentPrice(info.getCurrent_price().getValue());
			price.setLastModified(new Date());
			
			if (priceRepository.doesProductExists(info.getId())) {
				priceRepository.updatePriceInfo(price);
			} 
			else {
				priceRepository.save(price);
			}
		}
		catch (InvalidDataException t) {
			LOGGER.error("Exception! savePriceForProduct for " + info + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw t;
		}
		catch (Throwable t) {
			LOGGER.error("Exception! savePriceForProduct for " + info + ", reason : "+ ExceptionUtil.stackTraceToString(t));
			throw new InternalServiceException(t.getMessage(), t);
		}
	}
	
	/*
	 * Validate the data sent to the service for persistence
	 * @param The product id (entity identifier)
	 * @param ProductPricingInfo the price information for a product to be persisted to the database
	 */
	private void validate (String productId, ProductPricingInfo info) throws InvalidDataException
	{
		StringBuilder msg = new StringBuilder();
		if (info == null) {
			msgAppend(msg, "input is null");
		}
		else {
			if (info.getId() == null || info.getId().trim().isEmpty()) {
				msgAppend(msg, "Product id is missing");
			} else {
				if (productId == null || productId.trim().isEmpty()) {
					msgAppend(msg, "Product id is missing in the URL");
				} else {
					if (!productId.equals(info.getId())) {
						msgAppend(msg, "Mismatch in the product ID in URL and JSON body");
					}
				}
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
			throw new InvalidDataException(msg.toString());
		}
	}
	
	private void msgAppend(StringBuilder sb, String msg) {
		sb.append((sb.length() > 0) ? ", " : "");
		sb.append(msg);
	}
}
