package com.dmakarov.service;

import com.dmakarov.model.DeploymentEntity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

/**
 * Service handles all operations on Kubernetes resources.
 */
public interface KubernetesService {

  void deploy(DeploymentEntity deploymentEntity);

  void updateDeployment(DeploymentEntity deploymentEntity);

  boolean deleteDeployment(DeploymentEntity deploymentEntity);

  Deployment getDeployment(String namespace, String deploymentName);

  DeploymentList getDeployments(String namespace);
}
