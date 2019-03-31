package com.myretail.pricingservice.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Price {
	private String id;
	private PricingInfo currentPrice;
	private List<PricingInfo> priceHistory;
	
	@Indexed
	private String productId; // create a mongoDB index for the field productId to enable faster searches
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public PricingInfo getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(PricingInfo currentPrice) {
		this.currentPrice = currentPrice;
	}

	public List<PricingInfo> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<PricingInfo> priceHistory) {
		this.priceHistory = priceHistory;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
