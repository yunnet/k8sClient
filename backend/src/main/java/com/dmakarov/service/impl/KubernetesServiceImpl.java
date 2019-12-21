package com.dmakarov.service.impl;

import static com.dmakarov.deployment.DeploymentFactory.DEPLOYMENT_FUNCTION;

import com.dmakarov.model.DeploymentEntity;
import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesServiceImpl implements KubernetesService {
  private final Config config;
  private final Executor asyncExecutor;

  @Override
  public CompletableFuture<Void> deployAsync(DeploymentEntity deploymentEntity) {
    return CompletableFuture.runAsync(() -> deploy(deploymentEntity), asyncExecutor);
  }

  @Override
  public CompletableFuture<Void> updateDeploymentAsync(DeploymentEntity deploymentEntity) {
    return CompletableFuture.runAsync(() -> updateDeployment(deploymentEntity), asyncExecutor);
  }

  @Override
  public CompletableFuture<Void> deleteDeploymentAsync(DeploymentEntity deploymentEntity) {
    return CompletableFuture.runAsync(() -> deleteDeployment(deploymentEntity), asyncExecutor);
  }

  @Override
  public Deployment getDeployment(String namespace, String deploymentName) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).withName(deploymentName).get();
    } catch (KubernetesClientException e) {
      log.error("K8s get request error {}", e.getMessage());
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  @Override
  public DeploymentList getDeployments(String namespace) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      return client.apps().deployments().inNamespace(namespace).list();
    } catch (KubernetesClientException e) {
      log.error("K8s get request error {}", e.getMessage());
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  private void deploy(DeploymentEntity deploymentEntity) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      Deployment deployment = DEPLOYMENT_FUNCTION.apply(deploymentEntity);

      Deployment createdDeployment = client.apps().deployments()
          .inNamespace(deploymentEntity.getNamespace())
          .create(deployment);
      log.info("Created Deployment {}", createdDeployment);
    } catch (KubernetesClientException e) {
      log.error("K8s create request error {}", e.getMessage());
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  private void updateDeployment(DeploymentEntity deploymentEntity) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      client.apps().deployments()
          .inNamespace(deploymentEntity.getNamespace())
          .withName(deploymentEntity.getName())
          .edit()
          .editSpec()
          .withReplicas(deploymentEntity.getReplicasCount())
          .endSpec()
          .done();

      log.info("Updated Deployment {}", deploymentEntity.getName());
    } catch (KubernetesClientException e) {
      log.error("K8s update request error {}", e.getMessage());
      throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, "K8s request error");
    }
  }

  private void deleteDeployment(DeploymentEntity deploymentEntity) {
    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
      client.apps().deployments()
          .inNamespace(deploymentEntity.getNamespace())
          .withName(deploymentEntity.getName())
          .delete();

      log.info("Deployment deleted: {}", deploymentEntity.getName());
    } catch (KubernetesClientException e) {
      log.error("K8s delete request error {}", e.getMessage());
    }
  }

}
