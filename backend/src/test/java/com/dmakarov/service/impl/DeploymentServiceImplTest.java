package com.dmakarov.service.impl;

import static com.dmakarov.DeploymentUtils.getDeployment;
import static com.dmakarov.DeploymentUtils.getDeploymentDto;
import static com.dmakarov.DeploymentUtils.getDeploymentEntity;
import static com.dmakarov.DeploymentUtils.getDeploymentList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dmakarov.dao.DeploymentRepository;
import com.dmakarov.model.DeploymentEntity;
import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
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
  void createDeployment_newDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");

    when(repository.save(any(DeploymentEntity.class))).thenReturn(deploymentEntity);
    when(kubernetesService.deployAsync(any(DeploymentEntity.class)))
        .thenReturn(new CompletableFuture<>());

    DeploymentDto deployment = service.createDeployment(deploymentDto.getNamespace(),
        deploymentDto);

    verify(kubernetesService).deployAsync(any(DeploymentEntity.class));
    assertThat(deployment.getName(), is(deploymentDto.getName()));
  }

  @Test
  void createDeployment_erroredDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "ERRORED");

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);

    assertThrows(ClientException.class, () -> service.createDeployment(deploymentDto.getNamespace(),
        deploymentDto));
  }

  @Test
  void createDeployment_createdDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "CREATED");

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);

    DeploymentDto deployment = service.createDeployment(deploymentDto.getNamespace(),
        deploymentDto);

    verify(kubernetesService, times(0)).deployAsync(any(DeploymentEntity.class));
    assertThat(deployment.getName(), is(deploymentDto.getName()));
  }

  @Test
  void createDeployment_deployedDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);

    assertThrows(ClientException.class, () -> service.createDeployment(deploymentDto.getNamespace(),
        deploymentDto));
  }

  @Test
  void updateDeployment() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");

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
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");

    when(repository.findByNamespaceAndName(anyString(), anyString())).thenReturn(deploymentEntity);
    when(kubernetesService.deleteDeploymentAsync(any(DeploymentEntity.class)))
        .thenReturn(new CompletableFuture<>());

    service.deleteDeployment(deploymentDto.getNamespace(),
        deploymentDto.getName());

    verify(kubernetesService).deleteDeploymentAsync(any(DeploymentEntity.class));
  }

  @Test
  void getDeployment_success() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");
    Deployment deployment = getDeployment(deploymentEntity);

    when(kubernetesService.getDeployment(anyString(), anyString()))
        .thenReturn(deployment);

    Optional<DeploymentDto> result = service
        .getDeployment(deploymentDto.getNamespace(), deploymentDto.getName());

    verify(kubernetesService).getDeployment(anyString(), anyString());
    assertThat(result.get().getName(), is(deploymentDto.getName()));
  }

  @Test
  void getDeployments_success() {
    DeploymentDto deploymentDto = getDeploymentDto();
    DeploymentEntity deploymentEntity = getDeploymentEntity(deploymentDto, "DEPLOYED");
    DeploymentList deploymentList = getDeploymentList(deploymentEntity);

    when(kubernetesService.getDeployments(anyString()))
        .thenReturn(deploymentList);

    List<DeploymentDto> result = service.getDeployments(deploymentDto.getNamespace());

    verify(kubernetesService).getDeployments(anyString());
    assertThat(result.size(), is(1));
  }

}
