package com.myretail.pricingservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/*
 * Schema supported by our service's API for GET/PUT methods
 */
public class ProductPricingInfo {
	private String id;
	private String name;
	private PricingInfo current_price;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PricingInfo getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(PricingInfo current_price) {
		this.current_price = current_price;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
