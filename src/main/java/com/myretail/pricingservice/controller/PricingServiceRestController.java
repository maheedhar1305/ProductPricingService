package com.myretail.pricingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.service.PricingService;

@RestController
public class PricingServiceRestController {
	
	@Autowired
	private PricingService pricingService;
	
	@RequestMapping(value = "products/{id}", 
			method = RequestMethod.GET,
			produces = "application/json")
	@ResponseBody
    public ProductPricingInfo getInfo(@PathVariable("id") String productId) throws Throwable {
		//TODO handle exception better
        return pricingService.getPriceInfoForProduct(productId);
    }
}
