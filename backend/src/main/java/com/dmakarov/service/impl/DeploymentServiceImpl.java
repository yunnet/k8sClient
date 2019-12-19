package com.dmakarov.service.impl;

import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.DeploymentService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeploymentServiceImpl implements DeploymentService {
  private final Config config;

  @Override
  public DeploymentDto createDeployment(String namespace, DeploymentDto deploymentDto) {
    return DeploymentDto.builder()
        .namespace(deploymentDto.getNamespace())
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .build();
  }

  @Override
  public Optional<DeploymentDto> getDeployment(String namespace, String deploymentName) {
    Optional<DeploymentDto> deployment = Optional
        .ofNullable(retrieveDeployment(namespace, deploymentName))
        .map(dep -> DeploymentDto.builder()
            .namespace(dep.getMetadata().getNamespace())
            .name(dep.getMetadata().getName())
            .image(dep.getSpec().getTemplate().getSpec().getContainers().get(0).getImage())
            .build());

    return deployment.or(Optional::empty);
  }

  @Override
  public List<DeploymentDto> getDeployments(String namespace) {
    return retrieveDeployments(namespace).getItems().stream()
        .map(deployment ->
            DeploymentDto.builder()
                .namespace(deployment.getMetadata().getNamespace())
                .name(deployment.getMetadata().getName())
                .image(
                    deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage())
                .build())
        .collect(Collectors.toList());
  }

  private Deployment retrieveDeployment(String namespace, String deploymentName) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).withName(deploymentName).get();
    } catch (KubernetesClientException e) {
      log.error(e.getMessage(), e);
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  private DeploymentList retrieveDeployments(String namespace) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).list();
    } catch (KubernetesClientException e) {
      log.error(e.getMessage(), e);
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }
}
