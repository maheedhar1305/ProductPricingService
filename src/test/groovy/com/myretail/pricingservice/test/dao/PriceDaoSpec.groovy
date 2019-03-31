package com.myretail.pricingservice.test.dao

import org.junit.experimental.categories.Category
import org.springframework.data.mongodb.core.MongoTemplate
import com.myretail.pricingservice.dao.PriceDao
import com.myretail.pricingservice.dao.PriceRepository
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
	
	def 'update known product info'()
	{
		given : "price data for a product"
		    def price = new Price(productId : "345", currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD") 
			mongoTemplate.save price
		
		when: "update the current price of the product"
			priceDao.updateSalaryInfo(new Price(productId : "345",
								 		currentPrice : 101.01, currencyCode : "USD"))
			def result = mongoTemplate.findById(price.id, Price.class)
		
		then: "updates the data in the database"
			result
			result.currentPrice == 101.01
	}
	
	def 'test exists method'()
	{
		when:
			def result = priceDao.doesProductExists(input[0])
		
		then:
			result == expectedOutcome[0]
			
		where:
			 input       ||         expectedOutcome
		   ["345"]       ||         [ Boolean.TRUE ]
		   ["2341243"]   ||         [ Boolean.FALSE ]
	}
}
