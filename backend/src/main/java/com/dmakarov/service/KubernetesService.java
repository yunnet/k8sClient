package com.dmakarov.service;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

/**
 * Service handles all operations on Kubernetes resources.
 */
public interface KubernetesService {

  Deployment retrieveDeployment(String namespace, String deploymentName);

  DeploymentList retrieveDeployments(String namespace);
}
