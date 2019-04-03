package com.myretail.pricingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.myretail.pricingservice.properties.AuthProperties;

/*
 * Imposing security on the rest API
 */
@Configuration
@EnableWebSecurity
public class BasicApiSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthProperties authProperties;
	
	/*
	 * Create an admin user/password combo
	 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
        auth
          .inMemoryAuthentication()
          .withUser(authProperties.getAdminName())
            .password(encoder().encode(authProperties.getAdminPwd()))
            .roles("ADMIN");
    }
 
    /*
     * Any entity can access the GET API to consume information. 
     * Only entities that are authorized to do so (via ADMIN access), and have the credentials can update information.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "v1/products/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "v1/products/{id}").hasRole("ADMIN")
        .and()
        .httpBasic();
    	
    }
    
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}