package com.myretail.pricingservice.test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.myretail.pricingservice.controller.PricingServiceRestController;
import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.service.PricingService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(PricingServiceRestController.class)
public class PricingServiceRestControllerTest {
 
    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private PricingService pricingService;
 
    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray()
      throws Exception {
         
    	ProductPricingInfo info = new ProductPricingInfo();
    	info.setId("abc");
    	
    	given(pricingService.getPriceInfoForProduct("abc")).willReturn(info);
     
        mvc.perform(get("/products/abc")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("id", is("abc")));
    }
}
