package com.myretail.pricingservice.test;

/*
 * Interface to be used by org.junit.experimental.categories.Category
 * This helps gradle to run the entire test suite with a single command 
 * This way we can ensure the test suite is run everytime a "gradle build" is ran to create a new JAR
 * The JAR file will be built ONLY if the gradle succeeds in running all tests, without any issues
 */
public interface UnitTest {

}
