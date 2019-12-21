package com.dmakarov.service.impl;

import static com.dmakarov.deployment.DeploymentFactory.DEPLOYMENT_FUNCTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dmakarov.dao.DeploymentRepository;
import com.dmakarov.model.DeploymentEntity;
import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeploymentServiceImplTest {
  @Mock
  private DeploymentRepository repository;
  @Mock
  private KubernetesService kubernetesService;

  @InjectMocks
  private DeploymentServiceImpl service;

  @Test
  void createDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto);

    when(repository.save(any(DeploymentEntity.class))).thenReturn(deploymentEntity);
    when(kubernetesService.deployAsync(any(DeploymentEntity.class)))
        .thenReturn(new CompletableFuture<>());

    DeploymentDto deployment = service.createDeployment(deploymentDto.getNamespace(),
        deploymentDto);

    verify(kubernetesService).deployAsync(any(DeploymentEntity.class));
    assertThat(deployment.getName(), is(deploymentDto.getName()));
  }

  @Test
  void updateDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto);

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);
    when(repository.save(any(DeploymentEntity.class))).thenReturn(deploymentEntity);
    when(kubernetesService.updateDeploymentAsync(any(DeploymentEntity.class)))
        .thenReturn(new CompletableFuture<>());

    DeploymentDto deployment = service.updateDeployment(deploymentDto.getNamespace(),
        deploymentDto.getName(), deploymentDto);

    verify(kubernetesService).updateDeploymentAsync(any(DeploymentEntity.class));
    assertThat(deployment.getName(), is(deploymentDto.getName()));
  }

  @Test
  void deleteDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto);

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);
    when(kubernetesService.deleteDeploymentAsync(any(DeploymentEntity.class)))
        .thenReturn(new CompletableFuture<>());

    service.deleteDeployment(deploymentDto.getNamespace(),
        deploymentDto.getName());

    verify(kubernetesService).deleteDeploymentAsync(any(DeploymentEntity.class));
  }

  @Test
  void getDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto);
    Deployment deployment = getDeployment(deploymentEntity);

    when(kubernetesService.getDeployment(anyString(), anyString()))
        .thenReturn(deployment);

    Optional<DeploymentDto> result = service
        .getDeployment(deploymentDto.getNamespace(), deploymentDto.getName());

    verify(kubernetesService).getDeployment(anyString(), anyString());
    assertThat(result.get().getName(), is(deploymentDto.getName()));
  }

  @Test
  void getDeployments() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto);
    DeploymentList deploymentList = getDeploymentList(deploymentEntity);

    when(kubernetesService.getDeployments(anyString()))
        .thenReturn(deploymentList);

    List<DeploymentDto> result = service.getDeployments(deploymentDto.getNamespace());

    verify(kubernetesService).getDeployments(anyString());
    assertThat(result.size(), is(1));
  }

  private DeploymentDto getDeploymentDto() {
    int port = 8080;
    return DeploymentDto.builder()
        .namespace("namespace")
        .name("name")
        .image("image")
        .replicasCount(1)
        .port(port)
        .build();
  }

  private DeploymentEntity getDeploymentEntity(DeploymentDto deploymentDto) {
    return DeploymentEntity.builder()
        .namespace(deploymentDto.getNamespace())
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .replicasCount(deploymentDto.getReplicasCount())
        .commands(deploymentDto.getCommands())
        .args(deploymentDto.getArgs())
        .port(deploymentDto.getPort())
        .build();
  }

  private Deployment getDeployment(DeploymentEntity deploymentEntity) {
    Deployment deployment = DEPLOYMENT_FUNCTION.apply(deploymentEntity);
    DeploymentStatus deploymentStatus = new DeploymentStatus();
    deploymentStatus.setReadyReplicas(1);
    deploymentStatus.setReplicas(1);
    deployment.setStatus(deploymentStatus);
    return deployment;
  }

  private DeploymentList getDeploymentList(DeploymentEntity deploymentEntity) {
    Deployment deployment = getDeployment(deploymentEntity);
    DeploymentList deploymentList = new DeploymentList();
    deploymentList.setItems(List.of(deployment));
    return deploymentList;
  }
}