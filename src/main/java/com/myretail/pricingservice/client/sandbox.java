package com.myretail.pricingservice.client;

import org.springframework.web.client.RestTemplate;

import com.myretail.pricingservice.domain.InventoryInfo;

public class sandbox {
	
	public static void main(String args[]) {
		RestTemplate restTemplate = new RestTemplate();
		InventoryInfo quote = restTemplate.getForObject("https://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics", InventoryInfo.class);
		System.out.println(quote);
    }

}
