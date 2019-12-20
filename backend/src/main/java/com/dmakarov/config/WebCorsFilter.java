package com.dmakarov.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebCorsFilter extends CorsFilter {
  public WebCorsFilter() {
    super(configurationSource());
  }

  private static UrlBasedCorsConfigurationSource configurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // origins
    config.addAllowedOrigin("*");

    config.setAllowCredentials(true);

    // headers
    config.addAllowedHeader("Origin");
    config.addAllowedHeader("Authorization");
    config.addAllowedHeader("Content-Type");

    // methods
    config.addAllowedMethod(HttpMethod.OPTIONS);
    config.addAllowedMethod(HttpMethod.HEAD);
    config.addAllowedMethod(HttpMethod.GET);
    config.addAllowedMethod(HttpMethod.POST);
    config.addAllowedMethod(HttpMethod.PUT);
    config.addAllowedMethod(HttpMethod.DELETE);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
