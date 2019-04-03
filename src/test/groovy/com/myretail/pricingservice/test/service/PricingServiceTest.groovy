package com.myretail.pricingservice.test.service

import org.junit.experimental.categories.Category

import com.myretail.pricingservice.client.ProductServiceClient
import com.myretail.pricingservice.dao.PriceRepository
import com.myretail.pricingservice.domain.Description
import com.myretail.pricingservice.domain.InventoryInfo
import com.myretail.pricingservice.domain.Item
import com.myretail.pricingservice.domain.Price
import com.myretail.pricingservice.domain.PricingInfo
import com.myretail.pricingservice.domain.Product
import com.myretail.pricingservice.domain.ProductPricingInfo
import com.myretail.pricingservice.exception.ExternalCommsException
import com.myretail.pricingservice.exception.EntityNotFoundException
import com.myretail.pricingservice.exception.InternalServiceException
import com.myretail.pricingservice.exception.InvalidDataException
import com.myretail.pricingservice.service.PricingService
import com.myretail.pricingservice.service.PricingServiceImpl
import com.myretail.pricingservice.test.UnitTest
import javax.ws.rs.NotFoundException
import spock.lang.Specification

/*
 * Testing the PricingService
 */
@Category(UnitTest.class)
class PricingServiceTest extends Specification {
	PriceRepository priceRepository = Mock(PriceRepository)
	ProductServiceClient productServiceClient = Mock(ProductServiceClient)
	
	def "get pricing details for a valid product" ()
	{
		given : "a product in mongo and external repo"
			productServiceClient.getProductInfo("abc") >>
				new InventoryInfo(product : new Product(item : new Item(product_description : new Description(title : "product_abc"))))
				
			priceRepository.findByProductId("abc") >> new Price(productId : "abc", currentPrice : 101.01, currencyCode : "USD")
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when : "request for information"
			def result = service.getPriceInfoForProduct("abc");
		then : "get the data"
			result
			result.current_price
			result.id == "abc"
			result.name == "product_abc"
			result.current_price.value == 101.01
			result.current_price.currency_code == "USD"
	}
	
	def "get pricing details for a product not available in inventory" ()
	{
		given :
			productServiceClient.getProductInfo("abc") >> { throw new NotFoundException() }
			priceRepository.findByProductId("abc") >> new Price(productId : "abc", currentPrice : 101.01, currencyCode : "USD")
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			def result = service.getPriceInfoForProduct("abc");
		then :
			thrown EntityNotFoundException
	}
	
	def "get pricing details for a product not available in mongo" ()
	{
		given :
			productServiceClient.getProductInfo("abc") >> new InventoryInfo()
			priceRepository.findByProductId("abc") >> null
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			def result = service.getPriceInfoForProduct("abc");
		then :
			thrown EntityNotFoundException
	}
	
	def "server side error in the external api handled by service" ()
	{
		given :
			productServiceClient.getProductInfo("abc") >> { throw new ExternalCommsException() }
			priceRepository.findByProductId("abc") >> null
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			def result = service.getPriceInfoForProduct("abc");
		then : "The exception is bubbled up to be handled by rest exception error handler"
			thrown ExternalCommsException
	}
	
	def "misc internal exception handled by service" ()
	{
		given :
			productServiceClient.getProductInfo("abc") >>  new InventoryInfo()
			priceRepository.findByProductId("abc") >> { throw new Exception()}
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			def result = service.getPriceInfoForProduct("abc");
		then : "The exception is bubbled up to be handled by rest exception error handler"
			thrown InternalServiceException
	}
	
	def "validation tests for the save price details method" () {
		given :
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			service.savePriceForProduct(input[0], input[1])
		then :
			Exception ex = thrown(InvalidDataException)
			ex.message == "One or more of the following issues have been found: " + expectedOutcome[0]
		where : 
			 input       																		||         expectedOutcome
		["hgf", null]																       	    ||         [ "input is null" ]
	    ["hgf", new ProductPricingInfo(id : "", name : "name",
				current_price : new PricingInfo(value : 10.09, currency_code : "USD"))]         ||         [ "Product id is missing" ]
		["", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : new PricingInfo(value : 10.09, currency_code : "USD"))]         ||         [ "Product id is missing in the URL" ]
		["d", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : new PricingInfo(value : 10.09, currency_code : "USD"))]         ||         [ "Mismatch in the product ID in URL and JSON body" ]
		["hgf", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : null)]													        ||         [ "Pricing info is missing" ]
		["hgf", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : new PricingInfo(value : 10.09, currency_code : null))]          ||         [ "Currency code is missing" ]
		["hgf", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : new PricingInfo(value : null, currency_code : "USD"))]          ||         [ "Price is missing" ]
		["hgf", new ProductPricingInfo(id : "hgf", name : "name",
				current_price : new PricingInfo(value : -10.09, currency_code : "USD"))]        ||         [ "Price must be greater than zero" ]
	}
	
	def "save price details of a known product" () {
		given :
			priceRepository.doesProductExists("hgf") >> true
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			service.savePriceForProduct("hgf", new ProductPricingInfo(id : "hgf", name : "name", 
																	current_price : new PricingInfo(value : 10.09, currency_code : "USD")))
		then :
			1 * priceRepository.updatePriceInfo(_)
	}
	
	def "save price details of an unknown product" () {
		given :
			priceRepository.doesProductExists("hgf") >> false
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			service.savePriceForProduct("hgf",new ProductPricingInfo(id : "hgf", name : "name",
																	current_price : new PricingInfo(value : 10.09, currency_code : "USD")))
		then :
			1 * priceRepository.save(_)
	}
}
