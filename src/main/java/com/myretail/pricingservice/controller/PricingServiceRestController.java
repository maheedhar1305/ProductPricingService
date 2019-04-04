package com.myretail.pricingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.service.PricingService;

/*
 * The rest controller defining the API endpoints
 */
@RestController
public class PricingServiceRestController {
	
	@Autowired
	private PricingService pricingService;
	
	/*
	 *  The GET /pricing/v1/products/{id} API, that retrieves product information
	 */
	@RequestMapping(value = "v1/products/{id}", 
			method = RequestMethod.GET,
			produces = "application/json")
	@ResponseBody
    public ProductPricingInfo getInfo(@PathVariable("id") String productId)
    throws Throwable
	{
        return pricingService.getPriceInfoForProduct(productId);
    }
	
	/*
	 *  The PUT /pricing/v1/products/{id} API, that updates product information
	 */
	@RequestMapping(value = "v1/products/{id}", 
			method = RequestMethod.PUT,
			consumes = "application/json",
			produces = "application/json")
    public void postData(@PathVariable("id") String productId, @RequestBody ProductPricingInfo info)
    throws Throwable
	{
        pricingService.savePriceForProduct(productId, info);
    }
}
