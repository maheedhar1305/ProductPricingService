package com.myretail.pricingservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myretail.pricingservice.domain.Price;

/*
 * DAO methods that are used to perform operations on the mongo repository
 * Includes out-of-the-box methods provided by spring-data-mongodb 
 * as well as custom methods (PriceDao implementation), defined to perform specific operations
 */
@Repository
public interface PriceRepository extends MongoRepository<Price, String>, PriceDao {
	public Price findByProductId(String productId);
}