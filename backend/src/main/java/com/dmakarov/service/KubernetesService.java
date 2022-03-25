package com.dmakarov.service;

import com.dmakarov.model.DeploymentEntity;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service handles all operations on Kubernetes resources.
 */
public interface KubernetesService {

  /**
   * Creates K8S deployment in async mode.
   * @return completable future for async result processing
   */
  CompletableFuture<Void> deployAsync(DeploymentEntity deploymentEntity);

  /**
   * Updates K8S deployment replica count in async mode.
   * @return completable future for async result processing
   */
  CompletableFuture<Void> updateDeploymentAsync(DeploymentEntity deploymentEntity);

  /**
   * Deletes K8S deployment in async mode.
   * @return completable future for async result processing
   */
  CompletableFuture<Void> deleteDeploymentAsync(DeploymentEntity deploymentEntity);

  /**
   * Returns K8S deployment by provided namespace and deployment name.
   * @return deployment
   */
  Deployment getDeployment(String namespace, String deploymentName);

  /**
   * Returns K8S deployments by provided namespace.
   * @return namespace deployments
   */
  DeploymentList getDeployments(String namespace);

  List<Namespace> getNamespaceList();
}
