package com.dmakarov.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "k8s")
public class K8sProperties {

  private String endpoint;

  private String defaultNamespace;

  private String authToken;
}
