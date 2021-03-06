package com.myretail.pricingservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;
import com.myretail.pricingservice.domain.Price;

/*
 * PriceDao Implementation specific to MongoDB, uses spring-data-mongoDB.
 */
@Component
public class PriceDaoImpl implements PriceDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceDaoImpl.class);
	
    private final MongoTemplate mongoTemplate;

    @Autowired
	public PriceDaoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

    /*
     * Updates a record in mongo
     * @param price has the new data
     */
	@Override
	public void updatePriceInfo(Price price) {
		Criteria cri = Criteria.where("productId").is(price.getProductId());
		Query q = new Query(cri);
		
		Update update = new Update();
		update.set("currentPrice", price.getCurrentPrice());
		update.set("lastModified", price.getLastModified());
		update.set("currencyCode", price.getCurrencyCode());
		
		UpdateResult result = mongoTemplate.updateFirst(q, update, Price.class);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Update for " + price + " resulted in "+ result.getModifiedCount() +"modified records");
		}
	}

	/*
     * Check if a record identified by a given id, exists in mongo
     * @param productId is a unique id of the record in mongo
     */
	@Override
	public Boolean doesProductExists(String productId) {
		Criteria cri = Criteria.where("productId").is(productId);
		Query q = new Query(cri);
		
		Boolean result = mongoTemplate.exists(q, Price.class);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("doesProductExists for " + productId + " = "+ result);
		}
		
		return result;
	}

}
