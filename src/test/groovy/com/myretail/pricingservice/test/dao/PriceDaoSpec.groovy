package com.myretail.pricingservice.test.dao

import org.junit.experimental.categories.Category
import org.springframework.data.mongodb.core.MongoTemplate
import com.myretail.pricingservice.dao.PriceDao
import com.myretail.pricingservice.dao.PriceDaoImpl
import com.myretail.pricingservice.dao.PriceRepository
import com.myretail.pricingservice.domain.Price
import com.myretail.pricingservice.domain.PricingInfo
import com.myretail.pricingservice.test.UnitTest

import spock.lang.Shared
import spock.lang.Specification

/*
 * Testing the PriceDao interface
 */
@Category(UnitTest.class)
class PriceDaoSpec extends Specification {

	@Shared
	MongoTemplate mongoTemplate
	
	@Shared
	PriceDao priceDao
	
	/*
	 * Will be setup before any unit test is run
	 */
	def setupSpec() {
		mongoTemplate = FlapDoodleHelper.setup()
		priceDao = new PriceDaoImpl(mongoTemplate)
	}
	
	/*
	 * Will be cleaned up after the test suite completes running
	 */
	def cleanupSpec()
	{
		FlapDoodleHelper.cleanup()
	}
	
	def 'update_known_product_info'()
	{
		given : "price data for a product"
		    def price = new Price(productId : "345", currentPrice : 100.01, lastModified : new Date(), currencyCode : "USD") 
			mongoTemplate.save price
		
		when: "update the current price of the product"
			priceDao.updatePriceInfo(new Price(productId : "345",
								 		currentPrice : 101.01, currencyCode : "USD"))
			def result = mongoTemplate.findById(price.id, Price.class)
		
		then: "updates the data in the database"
			result
			result.currentPrice == 101.01
	}
	
	def 'test_exists_method'()
	{
		when:
			def result = priceDao.doesProductExists(input[0])
		
		then:
			result == expectedOutcome[0]
			
		where: "test both positive and negative cases"
			 input       ||         expectedOutcome
		   ["345"]       ||         [ Boolean.TRUE ]
		   ["2341243"]   ||         [ Boolean.FALSE ]
	}
}
