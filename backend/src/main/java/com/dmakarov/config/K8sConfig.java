package com.dmakarov.config;

import com.dmakarov.config.properties.K8sProperties;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
@RequiredArgsConstructor
public class K8sConfig {

  private final K8sProperties properties;

  /**
   * Kubernetes cluster client configuration.
   */
  @Bean
  public Config kubernetesConfig() {
    return new ConfigBuilder()
        .withMasterUrl(properties.getEndpoint())
        .withNamespace(properties.getDefaultNamespace())
        .withConnectionTimeout(10000)
        .build();
  }

}
