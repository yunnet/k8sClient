package com.dmakarov.service.impl;

import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesServiceImpl implements KubernetesService {
  private final Config config;

  @Override
  public Deployment retrieveDeployment(String namespace, String deploymentName) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).withName(deploymentName).get();
    } catch (KubernetesClientException e) {
      log.error(e.getMessage(), e);
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  @Override
  public DeploymentList retrieveDeployments(String namespace) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).list();
    } catch (KubernetesClientException e) {
      log.error(e.getMessage(), e);
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }
}
