package com.dmakarov.config;

import static com.dmakarov.ApiPathsV1.H2_CONSOLE;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .and()
        .csrf().disable()
        .authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests()
        .antMatchers(H2_CONSOLE + "/**").permitAll();

    http.headers().frameOptions().disable();
  }

}
