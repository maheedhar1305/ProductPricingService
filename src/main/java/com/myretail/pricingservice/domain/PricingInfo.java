package com.myretail.pricingservice.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PricingInfo {
	private Double currentPrice;
	private Date lastModified;
	private String currencyCode;
	
	public Double getCurrentPrice() {
		return currentPrice;
	}
	
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
