package com.myretail.pricingservice.test.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.*;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myretail.pricingservice.controller.PricingServiceRestController;
import com.myretail.pricingservice.controller.RestResponseEntityExceptionHandler;
import com.myretail.pricingservice.domain.ProductPricingInfo;
import com.myretail.pricingservice.service.PricingService;
import com.myretail.pricingservice.exception.*;

@RunWith(MockitoJUnitRunner.class)
public class PricingRestControllerTest {
 
    private MockMvc mvc;
 
    @Mock
    private PricingService pricingService;
 
    @InjectMocks
    private PricingServiceRestController restController;
 
    private JacksonTester<ProductPricingInfo> jsonTester;
 
    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(restController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }
 
    @Test
    public void canRetrieveByIdWhenExists() throws Exception {
    	
    	// given
    	ProductPricingInfo info = new ProductPricingInfo();
    	info.setId("abc");
    	given(pricingService.getPriceInfoForProduct("abc")).willReturn(info);
 
        // when
        MockHttpServletResponse response =  mvc.perform(get("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON))
								                .andReturn().getResponse();
 
        // then
        assertThat(response.getStatus(),is(HttpStatus.OK.value()));
        assertThat(response.getContentAsString(), is(jsonTester.write(info).getJson()));
    }
    
   @Test
    public void whenResourceNotFound_get404()
      throws Exception {
         
    	given(pricingService.getPriceInfoForProduct("abc")).willThrow(new NotFoundException());
    	
    	MockHttpServletResponse response =  mvc.perform(get("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON))
								                .andReturn().getResponse();
        
        assertThat(response.getStatus(),is(HttpStatus.NOT_FOUND.value()));
    }
    
    @Test
    public void whenServerSideException_get500()
      throws Exception {
         
    	given(pricingService.getPriceInfoForProduct("abc")).willThrow(new ServerSideException());
    	
    	MockHttpServletResponse response =  mvc.perform(get("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON))
								                .andReturn().getResponse();
        
        assertThat(response.getStatus(),is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
    
    @Test
    public void whenInternalException_get500()
      throws Exception {
         
    	given(pricingService.getPriceInfoForProduct("abc")).willThrow(new InternalServiceException());
    	
    	MockHttpServletResponse response =  mvc.perform(get("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON))
								                .andReturn().getResponse();
        
        assertThat(response.getStatus(),is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
    
    @Test
    public void whenPutPrice_thensuccess()
      throws Exception {
         
    	ProductPricingInfo info = new ProductPricingInfo();
    	info.setId("abc");
    	
    	mvc.perform(put("/v1/products/abc")
        		.contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(info)))
                .andExpect(status().isOk());
    }
    
    @Test
    public void whenInvalidRequestOnPut_get400()
      throws Exception {
    	
    	ProductPricingInfo info = new ProductPricingInfo();
    	info.setId("abc");
         
    	doThrow(BadRequestException.class)
		    	.when(pricingService)
		    	.savePriceForProduct(eq("abc"), Mockito.any(ProductPricingInfo.class));
    	
    	MockHttpServletResponse response =  mvc.perform(put("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON)
    											.content(new ObjectMapper().writeValueAsBytes(info)))
								                .andReturn().getResponse();
        
        assertThat(response.getStatus(),is(HttpStatus.BAD_REQUEST.value()));
    }
    
    @Test
    public void whenInternalErrorOnPut_get500()
      throws Exception {
    	
    	ProductPricingInfo info = new ProductPricingInfo();
    	info.setId("abc");
         
    	doThrow(InternalServiceException.class)
		    	.when(pricingService)
		    	.savePriceForProduct(eq("abc"), Mockito.any(ProductPricingInfo.class));
    	
    	MockHttpServletResponse response =  mvc.perform(put("/v1/products/abc")
								                .contentType(MediaType.APPLICATION_JSON)
    											.content(new ObjectMapper().writeValueAsBytes(info)))
								                .andReturn().getResponse();
        
        assertThat(response.getStatus(),is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}