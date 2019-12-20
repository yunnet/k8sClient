package com.dmakarov.service;

import com.dmakarov.model.dto.DeploymentDto;
import java.util.List;
import java.util.Optional;

/**
 * Service handles all operations on Kubernetes Deployments.
 */
public interface DeploymentService {
  DeploymentDto createDeployment(String namespace, DeploymentDto deploymentDto);

  DeploymentDto updateDeployment(String namespace, String deploymentName,
      DeploymentDto deploymentDto);

  void deleteDeployment(String namespace, String deploymentName);

  Optional<DeploymentDto> getDeployment(String namespace, String deploymentName);

  List<DeploymentDto> getDeployments(String namespace);
}
