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

    DeploymentEntity deploymentEntity = repository.save(newDeploymentEntity);



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

  @Override
  public Optional<DeploymentDto> getDeployment(String namespace, String deploymentName) {
    Optional<DeploymentDto> deployment = Optional
        .ofNullable(kubernetesService.retrieveDeployment(namespace, deploymentName))
        .map(dep -> DeploymentDto.builder()
            .namespace(dep.getMetadata().getNamespace())
            .name(dep.getMetadata().getName())
            .image(dep.getSpec().getTemplate().getSpec().getContainers().get(0).getImage())
            .build());

    return deployment.or(Optional::empty);
  }

  @Override
  public List<DeploymentDto> getDeployments(String namespace) {
    return kubernetesService.retrieveDeployments(namespace).getItems().stream()
        .map(deployment ->
            DeploymentDto.builder()
                .namespace(deployment.getMetadata().getNamespace())
                .name(deployment.getMetadata().getName())
                .image(
                    deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage())
                .status(getStatus(deployment))
                .build())
        .collect(Collectors.toList());
  }

  private String getStatus(Deployment deployment) {
    return deployment.getStatus().getReadyReplicas() + "/" + deployment.getStatus().getReplicas();
  }
}
