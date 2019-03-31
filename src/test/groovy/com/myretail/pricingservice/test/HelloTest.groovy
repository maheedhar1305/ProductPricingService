package com.myretail.pricingservice.test;

import org.junit.experimental.categories.Category

@Category(UnitTest.class)
class HelloTest extends spock.lang.Specification{
	def "Multiply 2 numbers"() {
		when : "a new number"
		def multi = 10;
		
		then : "3 times multi is 30"
		multi*3 == 30;
	}
}
