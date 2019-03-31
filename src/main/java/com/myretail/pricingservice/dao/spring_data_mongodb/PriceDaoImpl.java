package com.myretail.pricingservice.dao.spring_data_mongodb;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.myretail.pricingservice.dao.PriceDao;
import com.myretail.pricingservice.domain.Price;
import com.myretail.pricingservice.domain.PricingInfo;

@Repository
public class PriceDaoImpl implements PriceDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceDaoImpl.class);
	
    private final MongoTemplate mongoTemplate;

    @Autowired
	public PriceDaoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
    }
	
	@Override
	public void updatePricesForProduct(String productId, PricingInfo info) {
		
	}

	@Override
	public PricingInfo findCurrentPriceForProduct(String productId) {
		Criteria cri = Criteria.where("productId").is(productId);
		
		Query q = new Query(cri);
		q.fields().exclude("priceHistory");
		
		Price result = mongoTemplate.findOne(q, Price.class);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Price data for productId : " + productId + " is : "+ result);
		}
		
		return Optional.ofNullable(result)
				       .map(Price::getCurrentPrice)
				       .orElse(null);
	}

	@Override
	public Boolean productDataExists(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Price data) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("save price data : " + data);
		}
		
		mongoTemplate.save(data);
	}
}
