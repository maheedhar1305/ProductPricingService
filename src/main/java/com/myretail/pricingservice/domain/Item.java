package com.myretail.pricingservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	private Description product_description;

	public Description getProduct_description() {
		return product_description;
	}

	public void setProduct_description(Description product_description) {
		this.product_description = product_description;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
