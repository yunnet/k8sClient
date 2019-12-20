package com.dmakarov.service.impl;

import com.dmakarov.dao.DeploymentRepository;
import com.dmakarov.model.DeploymentEntity;
import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.DeploymentService;
import com.dmakarov.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
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
  public static final String STATUS_SEPARATOR = "/";
  private final DeploymentRepository repository;
  private final KubernetesService kubernetesService;

  @Override
  public DeploymentDto createDeployment(String namespace, DeploymentDto deploymentDto) {
    DeploymentEntity existedDeployment = repository
        .findByNamespaceAndName(namespace, deploymentDto.getName());

    if (existedDeployment != null) {
      throw new ClientException(HttpStatus.BAD_REQUEST, "Deployment in provided namespace with "
          + "this name already exist");
    }

    DeploymentEntity newDeploymentEntity = DeploymentEntity.builder()
        .namespace(namespace)
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .replicasCount(deploymentDto.getReplicasCount())
        .commands(deploymentDto.getCommands())
        .args(deploymentDto.getArgs())
        .port(deploymentDto.getPort())
        .build();

    // Deploy to K8s, if no errors or exceptions save entity
    kubernetesService.deploy(newDeploymentEntity);

    DeploymentEntity deploymentEntity = repository.save(newDeploymentEntity);

    return getDeploymentDto(deploymentEntity);
  }

  @Override
  public DeploymentDto updateDeployment(String namespace, String deploymentName,
      DeploymentDto deploymentDto) {
    DeploymentEntity existedDeployment = repository
        .findByNamespaceAndName(namespace, deploymentName);

    if (existedDeployment == null) {
      throw new ClientException(HttpStatus.BAD_REQUEST, "Deployment not found");
    }

    DeploymentEntity updatedDeploymentEntity = DeploymentEntity.builder()
        .id(existedDeployment.getId())
        .namespace(existedDeployment.getNamespace())
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .replicasCount(deploymentDto.getReplicasCount())
        .commands(deploymentDto.getCommands())
        .args(deploymentDto.getArgs())
        .port(deploymentDto.getPort())
        .build();

    // Deploy to K8s, if no errors or exceptions save entity
    kubernetesService.updateDeployment(updatedDeploymentEntity);

    DeploymentEntity deploymentEntity = repository.save(updatedDeploymentEntity);

    return getDeploymentDto(deploymentEntity);
  }

  @Override
  public void deleteDeployment(String namespace, String deploymentName) {
    DeploymentEntity existedDeployment = repository
        .findByNamespaceAndName(namespace, deploymentName);

    if (existedDeployment == null) {
      throw new ClientException(HttpStatus.BAD_REQUEST, "Deployment not found");
    }

    // Delete DEPLOYMENT_FUNCTION in K8s, if no errors or exceptions delete entity
    kubernetesService.deleteDeployment(existedDeployment);

    repository.delete(existedDeployment);
  }

  @Override
  public Optional<DeploymentDto> getDeployment(String namespace, String deploymentName) {
    Optional<DeploymentDto> deployment = Optional
        .ofNullable(kubernetesService.getDeployment(namespace, deploymentName))
        .map(dep -> DeploymentDto.builder()
            .namespace(dep.getMetadata().getNamespace())
            .name(dep.getMetadata().getName())
            .image(dep.getSpec().getTemplate().getSpec().getContainers().get(0).getImage())
            .status(getStatus(dep))
            .replicasCount(dep.getSpec().getReplicas())
            .port(
                dep.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts()
                    .get(0).getContainerPort()
            )
            .build());

    return deployment.or(Optional::empty);
  }

  @Override
  public List<DeploymentDto> getDeployments(String namespace) {
    return kubernetesService.getDeployments(namespace).getItems().stream()
        .map(deployment ->
            DeploymentDto.builder()
                .namespace(deployment.getMetadata().getNamespace())
                .name(deployment.getMetadata().getName())
                .image(
                    deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage()
                )
                .status(getStatus(deployment))
                .replicasCount(deployment.getSpec().getReplicas())
                .port(
                    deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts()
                        .get(0).getContainerPort()
                )
                .build())
        .collect(Collectors.toList());
  }

  private String getStatus(Deployment deployment) {
    return deployment.getStatus().getReadyReplicas()
        + STATUS_SEPARATOR
        + deployment.getStatus().getReplicas();
  }

  private DeploymentDto getDeploymentDto(DeploymentEntity deploymentEntity) {
    return DeploymentDto.builder()
        .namespace(deploymentEntity.getNamespace())
        .name(deploymentEntity.getName())
        .image(deploymentEntity.getImage())
        .replicasCount(deploymentEntity.getReplicasCount())
        .commands(deploymentEntity.getCommands())
        .args(deploymentEntity.getArgs())
        .port(deploymentEntity.getPort())
        .build();
  }
}
