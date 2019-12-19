package com.dmakarov.config;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Component
public class WebMvcFilter implements WebMvcConfigurer {
  private static final String BASE_API_PATH = "/api/billing";

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //All resources go to where they should go
    registry
        .addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.jsx",
            "/**/*.png", "/**/*.svg", "/**/*.ico", "/**/*.jpg", "/**/*.ttf", "/**/*.woff",
            "/**/*.woff2")
        .setCachePeriod(0)
        .addResourceLocations("classpath:/static/");

    registry.addResourceHandler("/**")
        .setCachePeriod(0)
        .addResourceLocations("classpath:/static/index.html")
        .resourceChain(true)
        .addResolver(new PathResourceResolver() {
          @Override
          protected Resource getResource(String resourcePath, Resource location) {
            if (resourcePath.startsWith(BASE_API_PATH)
                || resourcePath.startsWith(BASE_API_PATH.substring(1))) {
              return null;
            }

            return location.exists() && location.isReadable() ? location : null;
          }
        });
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/index.html");
  }
}
