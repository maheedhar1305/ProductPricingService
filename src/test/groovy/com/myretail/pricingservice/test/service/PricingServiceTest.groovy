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
import com.myretail.pricingservice.service.PricingService
import com.myretail.pricingservice.service.PricingServiceImpl
import com.myretail.pricingservice.test.UnitTest

import spock.lang.Specification

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
			productServiceClient.getProductInfo("abc") >> null
			priceRepository.findByProductId("abc") >> new Price(productId : "abc", currentPrice : 101.01, currencyCode : "USD")
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when : 
			def result = service.getPriceInfoForProduct("abc");
		then :
			result
			result.current_price
			result.id == "abc"
			result.name == null
			result.current_price.value == 101.01
			result.current_price.currency_code == "USD"
	}
	
	def "get pricing details for unknown product" ()
	{
		given :
			productServiceClient.getProductInfo("abc") >> new InventoryInfo()
			priceRepository.findByProductId("abc") >> null
			
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			def result = service.getPriceInfoForProduct("abc");
		then :
			result == null
	}
	
	def "save price details of a known product" () {
		given :
			priceRepository.doesProductExists("hgf") >> true
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			service.savePriceForProduct(new ProductPricingInfo(id : "hgf", name : "name", 
																	current_price : new PricingInfo(value : 10.09)))
		then :
			priceRepository.updateSalaryInfo(_) * 1
	}
	
	def "save price details of an unknown product" () {
		given :
			priceRepository.doesProductExists("hgf") >> false
			PricingService service = new PricingServiceImpl(productServiceClient : productServiceClient, priceRepository : priceRepository)
		when :
			service.savePriceForProduct(new ProductPricingInfo(id : "hgf", name : "name",
																	current_price : new PricingInfo(value : 10.09)))
		then :
			priceRepository.save(_) * 1
	}
}
