package com.myretail.pricingservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myretail.pricingservice.domain.Price;

@Repository
public interface PriceRepository extends MongoRepository<Price, String>, PriceDao {
	public Price findByProductId(String productId);
}