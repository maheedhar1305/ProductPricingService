package com.myretail.pricingservice.test.dao

import org.junit.experimental.categories.Category
import org.springframework.data.mongodb.core.MongoTemplate
import com.myretail.pricingservice.dao.PriceDao
import com.myretail.pricingservice.dao.spring_data_mongodb.PriceDaoImpl
import com.myretail.pricingservice.domain.Price
import com.myretail.pricingservice.domain.PricingInfo
import com.myretail.pricingservice.test.UnitTest

import spock.lang.Shared
import spock.lang.Specification

@Category(UnitTest.class)
class PriceDaoSpec extends Specification {

	@Shared
	MongoTemplate mongoTemplate
	
	@Shared
	PriceDao priceDao
	
	def setupSpec() {
		mongoTemplate = FlapDoodleHelper.setup()
		priceDao = new PriceDaoImpl(mongoTemplate)
	}
	
	def cleanupSpec()
	{
		FlapDoodleHelper.cleanup()
	}
	
	def 'save record'()
	{
		given : "price data for a product"
			def price = new Price(productId : "ABCD",
								currentPrice : new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD"),
																priceHistory : [ new PricingInfo(currentPrice : 99.01, lastModified : new Date(), currencyCode : "USD"),
																				new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD")])
			priceDao.save price
		
		when: "save to database"
			def result = mongoTemplate.findById(price.id, Price.class)
		
		then: "data is persisted"
			result
			result.productId == "ABCD"
	}
	
	def 'get pricingInfo for a known product'()
	{
		given : "price data for a product"
			priceDao.save new Price(productId : "12345", 
									 currentPrice : new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD"),
																	priceHistory : [ new PricingInfo(currentPrice : 99.01, lastModified : new Date(), currencyCode : "USD"),
																					new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD")])
		
		when: "query for product id"
			def result = priceDao.findCurrentPriceForProduct("12345")
		
		then: "current price is returned"
			result
			result.currentPrice == 100.01
			result.currencyCode == "USD"
	}
	
	def 'get pricingInfo for an unknown product'()
	{
		when: "query for product id that does not exist in the database"
			def result = priceDao.findCurrentPriceForProduct("2341243")
		
		then: "no data returned"
			result == null
	}
	
	def 'test exists method'()
	{
		when:
			def result = priceDao.productDataExists(input[0])
		
		then: 
			result == expectedOutcome[0]
			
		where:
			 input       ||         expectedOutcome
		   ["12345"]     ||         [ Boolean.TRUE ]
		   ["2341243"]   ||         [ Boolean.FALSE ]
	}
	
	def 'update known product info'()
	{
		given : "price data for a product"
			priceDao.save new Price(productId : "345",
								 		currentPrice : new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD"),
																priceHistory : [new PricingInfo(currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD")])
		
		when: "update the current price of the product"
			priceDao.updatePricesForProduct("345", new PricingInfo(currentPrice : 101.01, lastModified : new Date(), currencyCode : "USD"))
			def result = priceDao.findCurrentPriceForProduct("345")
		
		then: "updates the data in the database"
			result
			result.currentPrice == 101.01
	}
}
