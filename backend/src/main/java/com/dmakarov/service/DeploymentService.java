package com.dmakarov.service;

import com.dmakarov.model.dto.DeploymentDto;
import java.util.List;
import java.util.Optional;

/**
 * Service handles all operations on Kubernetes Deployments.
 */
public interface DeploymentService {
  /**
   * Creates new database entity and starts async deployment process.
   * @return deployment dto
   */
  DeploymentDto createDeployment(String namespace, DeploymentDto deploymentDto);

  /**
   * Updates database entity and starts async update deployment process.
   * @return deployment dto
   */
  DeploymentDto updateDeployment(String namespace, String deploymentName,
      DeploymentDto deploymentDto);

  /**
   * Deletes database entity and starts async delete deployment process.
   */
  void deleteDeployment(String namespace, String deploymentName);

  /**
   * Returns deployment by namespace and deployment name.
   */
  Optional<DeploymentDto> getDeployment(String namespace, String deploymentName);

  /**
   * Returns all namespace deployments.
   */
  List<DeploymentDto> getDeployments(String namespace);
}
