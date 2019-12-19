package com.dmakarov.service;

import com.dmakarov.model.Deployment;
import com.dmakarov.model.DeploymentDto;
import java.util.List;
import java.util.UUID;

/**
 * Service handles all operations on Kubernetes Deployments.
 */
public interface DeploymentService {
  Deployment createDeployment(DeploymentDto deploymentDto);

  Deployment getDeployment(UUID deploymentId);

  List<Deployment> getDeployments();
}
