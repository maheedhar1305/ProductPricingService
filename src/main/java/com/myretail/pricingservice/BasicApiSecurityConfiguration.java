package com.myretail.pricingservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class BasicApiSecurityConfiguration extends WebSecurityConfigurerAdapter {
	 
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
        auth
          .inMemoryAuthentication()
          .withUser("admin")
            .password("{noop}admin")
            .roles("ADMIN");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/products/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/products/{id}").hasRole("ADMIN")
        .and()
        .httpBasic();
    	
    }
}